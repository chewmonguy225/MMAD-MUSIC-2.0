package com.MMAD.MMAD.model.User;

import java.io.Serializable;
import java.util.Objects;

public class UserFriendPK  implements Serializable {

    private Long userId;

    private Long friendId;


    /**
     * Empty constructor for JPA.
     */
    public UserFriendPK() {
    }


    /**
     * Constructor taking all parameters.
     *
     * @param userId   The user's id.
     * @param friendId The friend's id.
     */
    public UserFriendPK(Long userId, Long friendId) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFriendPK that = (UserFriendPK) o;
        return Objects.equals(userId, that.userId) && Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }

}
