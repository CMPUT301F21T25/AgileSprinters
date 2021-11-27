package com.example.agilesprinters;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This is a test class which tests add, accept and declining of follow requests.
 *
 * @author Hannah Desmarais
 */
public class NotificationsUnitTest {
    private User currentUser;

    /**
     * This method creates a mock test notification list and sets the current user for user in the
     * tests.
     * @return
     * Returns the mock notifications list created.
     */
    private NotificationTestList mockNotificationList(){
        NotificationTestList notificationList = new NotificationTestList();

        currentUser = mockUser();
        currentUser.setUser("Current User");
        notificationList.addNotification(mockUser(), currentUser);

        return notificationList;
    }

    /**
     * This method creates a mock user.
     * @return
     * Returns the mock user created.
     */
    private User mockUser(){
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> following = new ArrayList<>();
        ArrayList<String> followerRequests = new ArrayList<>();
        User user = new User("Requesting User", "TestFN","TestLN", "Test@email.com"
                , followers, following, followerRequests);
        return user;
    }

    /**
     * This test will test adding a notification/follow request to the list.
     */
    @Test
    void testAddNotification(){
        NotificationTestList notificationTestList = mockNotificationList();
        assertTrue(notificationTestList.getRequestingUsers().size() == 1);

        User user = mockUser();
        user.setUser("Requesting User 2");
        notificationTestList.addNotification(user, currentUser);

        assertTrue(notificationTestList.getRequestingUsers().size() == 2);
        assertTrue(currentUser.getFollowRequestList().contains(user.getUserID()));
    }

    /**
     * This test will attempt to add follow requests which should result in exceptions being thrown.
     */
    @Test
    void testAddNotificationException(){
        NotificationTestList notificationTestList = mockNotificationList();
        assertTrue(notificationTestList.getRequestingUsers().size() == 1);

        User user = mockUser();
        user.setUser("Requesting User 2");
        notificationTestList.addNotification(user, currentUser);

        assertTrue(notificationTestList.getRequestingUsers().size() == 2);

        assertThrows(IllegalArgumentException.class, () ->{
           notificationTestList.addNotification(user, currentUser);
        });
    }

    /**
     * This will test that when a user accepts a follow request that all information is added to
     * the appropriate lists.
     */
    @Test
    void testAccept(){
        NotificationTestList notificationTestList = mockNotificationList();
        assertTrue(notificationTestList.getRequestingUsers().size() == 1);

        User requestingUser = notificationTestList.getRequestingUsers().get(0);
        notificationTestList.acceptUser(requestingUser, currentUser);

        assertTrue(notificationTestList.getRequestingUsers().size() == 0);
        assertTrue(currentUser.getFollowersList().contains(requestingUser.getUserID()));
        assertTrue(requestingUser.getFollowingList().contains(currentUser.getUserID()));
        assertTrue(currentUser.getFollowRequestList().size() == 0);
    }

    /**
     * This is a test which will check that appropriate excepts are thrown when performing invalid
     * accepts.
     */
    @Test
    void testAcceptExceptions(){
        NotificationTestList notificationTestList = mockNotificationList();
        assertTrue(notificationTestList.getRequestingUsers().size() == 1);

        User user = mockUser();
        user.setUser("Requesting User 2");

        assertThrows(IllegalArgumentException.class, () -> {
           notificationTestList.acceptUser(user, currentUser);
        });

        notificationTestList.getRequestingUsers().clear();

        assertThrows(IllegalArgumentException.class, () -> {
           notificationTestList.acceptUser(user, currentUser);
        });
    }

    /**
     * This will test that when a follow request is declined, that all lists are updated correctly.
     */
    @Test
    void testDecline(){
        NotificationTestList notificationTestList = mockNotificationList();
        assertTrue(notificationTestList.getRequestingUsers().size() == 1);

        User requestingUser = notificationTestList.getRequestingUsers().get(0);
        notificationTestList.declineUser(requestingUser, currentUser);

        assertTrue(notificationTestList.getRequestingUsers().size() == 0);
        assertTrue(!currentUser.getFollowersList().contains(requestingUser.getUserID()));
        assertTrue(!requestingUser.getFollowingList().contains(currentUser.getUserID()));
        assertTrue(currentUser.getFollowRequestList().size() == 0);
    }

    /**
     * This is a test which will check that the appropriate exceptions are thrown when an invalid
     * decline call is made.
     */
    @Test
    void testDeclineExceptions(){
        NotificationTestList notificationTestList = mockNotificationList();
        assertTrue(notificationTestList.getRequestingUsers().size() == 1);

        User user = mockUser();
        user.setUser("Requesting User 2");

        assertThrows(IllegalArgumentException.class, () -> {
            notificationTestList.declineUser(user, currentUser);
        });

        notificationTestList.getRequestingUsers().clear();

        assertThrows(IllegalArgumentException.class, () -> {
            notificationTestList.declineUser(user, currentUser);
        });
    }
}
