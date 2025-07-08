package com.MMAD.MMAD.model.User;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_friends")
@IdClass(value = UserFriendPK.class)
public class UserFriend implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "friend_id")
    private Long friendId;


    /**
     * Empty constructor for JPA.
     */
    public UserFriend() {
    }


    /**
     * Constructor taking all parameters.
     *
     * @param userId   The user's id.
     * @param friendId The friend's id.
     */
    public UserFriend(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }


    /**
     * Getters and setters.
     */

    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getFriendId() {
        return friendId;
    }


    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}
