package org.example.task3;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final List<UserFriend> userFriends = new ArrayList<>();
    private static final Random RANDOM = new Random();

    static {
        for (int i = 0; i < 1000; i++) {
            int userId = 1 + RANDOM.nextInt(100);
            int friendId = 1 + RANDOM.nextInt(100);
            userFriends.add(new UserFriend(userId, friendId));
        }
    }

    static Observable<UserFriend> getFriends(int userId) {
        return Observable.fromIterable(userFriends)
                .filter(uf -> uf.getUserId() == userId);
    }

    public static void main(String[] args) {
        int[] userIds = new int[20];
        for (int i = 0; i < userIds.length; i++) {
            userIds[i] = 1 + RANDOM.nextInt(100);
        }

        System.out.print("User IDs: ");
        for (int id : userIds) {
            System.out.print(id + " ");
        }
        System.out.println();

        Observable<Integer> userIdStream = Observable.fromArray(toIntegerArray(userIds))
                .subscribeOn(Schedulers.computation());

        Observable<UserFriend> friendsStream = userIdStream
                .flatMap(Main::getFriends);

        friendsStream.subscribe(
                uf -> System.out.println("Friend: " + uf),
                Throwable::printStackTrace,
                () -> System.out.println("Completed processing friends.")
        );

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Integer[] toIntegerArray(int[] arr) {
        Integer[] result = new Integer[arr.length];
        for(int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }
}
