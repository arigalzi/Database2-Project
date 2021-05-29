package it.polimi.db2_project.entities;

import javax.persistence.*;
import java.io.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "log", schema = "db2_app")
@NamedQuery(name = "Log.getCurrentLogOfUser", query = "SELECT l FROM Log l WHERE l.user= ?1 AND l.timestamp = (SELECT MAX(l.timestamp) FROM Log l WHERE l.user = ?1)")
public class Log implements Serializable {
    public Log() {
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    @Temporal(TemporalType.TIMESTAMP)//added this type on order to have the entire date including time of the day
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID", insertable = false, updatable = false)
    private User user;

    @NotNull
    private int userId;

    @NotNull
    @Column(name = "formCancelled")
    private boolean isFormCancelled;

    public boolean isFormCancelled() {
        return isFormCancelled;
    }

    public void setFormCancelled(boolean formCancelled) {
        isFormCancelled = formCancelled;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public Date getDate() {
        return timestamp;
    }

    public void setDate(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
