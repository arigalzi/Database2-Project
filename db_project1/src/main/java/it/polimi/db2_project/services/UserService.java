package it.polimi.db2_project.services;

//import it.polimi.db2_project.auxiliary.UserStatus;
import it.polimi.db2_project.auxiliary.UserStatus;
import it.polimi.db2_project.entities.*;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import javax.validation.constraints.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.EntityManager;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Stateless

public class UserService {
    @PersistenceContext(unitName = "db2_app")
    private EntityManager em ;

    public UserService() {
    }

    /**
     * Method to add a new User in the DB
     * @param username username of the User
     * @param email email of the User
     * @param password password of the User
     * @param banned ban flag of the User
     * @throws PersistenceException if a problem happens managing the entity (for example it already exists)
     * @throws IllegalArgumentException if the argument of the persist is not an entity
     */
    public User addUser(String username, String email, String password, boolean banned) throws PersistenceException, IllegalArgumentException{
        User user = new User();
        user.setBanned(banned);
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        em.persist(user);
        return user;
    }

    /**
     * Method to get a specific user
     * @param username username of the User to retrieve
     * @return the user requested
     * @throws InvalidParameterException if the user does not exist
     */
    public User getUser(String username) throws InvalidParameterException {
        List<User> usersFromDB = em.createNamedQuery("User.getUser", User.class).setParameter(1, username).getResultList();
        if (usersFromDB == null || usersFromDB.isEmpty()) {
            throw new InvalidParameterException("No User with this username");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to check the User credentials
     * @param username username of the User
     * @param password password of the User
     * @return User with corresponding fields
     * @throws InvalidParameterException if the user does not exist
     */
    public User checkCredentials(String username, String password) throws InvalidParameterException {
        List<User> usersFromDB = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2, password)
                .getResultList();
        if (usersFromDB == null || usersFromDB.isEmpty()) {
            throw new InvalidParameterException("Provided username or password is wrong");
        }
        else if(usersFromDB.size()==1) {
            return usersFromDB.get(0);
        }
        else {
            throw new InvalidParameterException("internal database error");
        }
    }

    /**
     * Method to ban the user
     * @param username username of the User
     * @throws PersistenceException if updateProfile fails
     * @throws IllegalArgumentException if the user does not exist
    **/
    public void banUser(String username) throws PersistenceException, IllegalArgumentException{
        User userToBan = getUser(username);
        userToBan.setBanned(true);
        em.merge(userToBan);
    }

    /**
     * Method to check the status of the user, in relationship with the product
     * @param user user to check
     * @param product product to compare with
     * @return userstatus of the user
     * @throws InvalidParameterException if there is a problem with the query execution
     **/
    public UserStatus checkUserStatus(User user, Product product) throws InvalidParameterException{
        if(user.isBanned()){
            return UserStatus.BANNED;
        }
        else{

            List<Answer> ans = em.createNamedQuery("Answer.getUserAnswers", Answer.class)
                    .setParameter(1, user.getUsername()).setParameter(2, product.getProductId()).getResultList();
            if (ans == null || ans.isEmpty()) {
                return UserStatus.NOT_COMPLETED;
            }
            else {
                return UserStatus.COMPLETED;
            }
        }
    }

    /**
     * method to insert the log of the user in the DB
     * @param user user to log in
     * @throws PersistenceException if there is a problem managing the entity
     * @throws IllegalArgumentException if the user is not present in the DB
     */
    public void LogUser(User user) throws PersistenceException, IllegalArgumentException{
        Log log = new Log();
        log.setUser(user);
        log.setUserId(user.getUserID());
        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //Date date = new Date(System.currentTimeMillis());
        log.setDate(new Date(System.currentTimeMillis()));
        em.persist(log);
    }


    public void cancelForm(User user){
        Log current_log = em.createNamedQuery("Log.getCurrentLogOfUser", Log.class).setParameter(1,user).getSingleResult();
        current_log.setFormCancelled(true);
        em.merge(current_log);
    }

    public List<String> getUsersWhoCanceled(Product product) {

        List<String> userCancString= new ArrayList<>();

        Calendar morningTime = Calendar.getInstance();
        morningTime.setTime(product.getDate());
        morningTime.set(Calendar.HOUR_OF_DAY, 0); // <-- here
        morningTime.set(Calendar.MINUTE, 0);
        morningTime.set(Calendar.SECOND, 1);
        SimpleDateFormat morningDate= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        morningDate.setCalendar(morningTime);

        Calendar nightTime = Calendar.getInstance();
        nightTime.setTime(product.getDate());
        nightTime.set(Calendar.HOUR_OF_DAY, 23); // <-- here
        nightTime.set(Calendar.MINUTE, 59);
        nightTime.set(Calendar.SECOND, 59);
        SimpleDateFormat nightDate= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        nightDate.setCalendar(nightTime);


        List<User> usersCan = em.createNamedQuery("User.getUsersCanceled", User.class).setParameter(1, morningDate).setParameter( 2, nightDate).getResultList();

        if (usersCan == null || usersCan.isEmpty()) {
            return null;
        }

        usersCan.stream().forEach(q->userCancString.add(q.getUsername()));
        return userCancString;

    }

    public List<String> getUsersWhoSubmits(Product product) {

        List<String> userSubString= new ArrayList<>();
        List<User> userSub = em.createNamedQuery("User.getUsersSubmits", User.class).setParameter(1, product.getProductId()).getResultList();

        if (userSub == null || userSub.isEmpty()) {
            return null;
        }

        userSub.stream().forEach(q->userSubString.add(q.getUsername()));
        return userSubString;

    }

    public List<String> getAnsweredQuestions(Product product,String username) {
        List<Question> questions = em.createNamedQuery("Answer.getAnsweredQuestions",Question.class).setParameter(1, product.getProductId()).setParameter(2,username).getResultList();
        questions.stream()
                .sorted(Comparator.comparing(n->n.getQuestionNumber()))
                .collect(Collectors.toList());
        List<String> results = new ArrayList<String>();
        questions.stream().forEach(q -> results.add(q.getText()));
        return results;
    }
}

