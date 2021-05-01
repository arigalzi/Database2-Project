package it.polimi.db2_project.services;

//import it.polimi.db2_project.auxiliary.UserStatus;
import it.polimi.db2_project.auxiliary.UserStatus;
import it.polimi.db2_project.entities.Answer;
import it.polimi.db2_project.entities.Log;
import it.polimi.db2_project.entities.Product;
import it.polimi.db2_project.entities.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import javax.validation.constraints.*;
import java.util.*;
import javax.persistence.EntityManager;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
        User user = getUser(username);
        user.setBanned(true);
        em.merge(user);
    }

    /**
     * Method to check the status of the user, in relationship with the product
     * @param user user to check
     * @param product product to compare with
     * @param productService utility to make query
     * @return userstatus of the user
     * @throws InvalidParameterException if there is a problem with the query execution
     **/
    public UserStatus checkUserStatus(User user, Product product, ProductService productService) throws InvalidParameterException{
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
        log.setDate(new Timestamp(System.currentTimeMillis()));
        em.persist(log);
    }


    public void cancelForm(User user){
        Log current_log = em.createNamedQuery("Log.getCurrentLogOfUser", Log.class).setParameter(1,user).getSingleResult();
        current_log.setFormCancelled(true);
        em.merge(current_log);
    }
}

