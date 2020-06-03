package xyz.probablynotkai;

import org.apache.http.HttpHost;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.*;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest.ShareType;
import org.brunocvcunha.instagram4j.requests.payload.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
{
    @lombok.SneakyThrows
    public static void main(String[] args) {
        Instagram4j instagram = Instagram4j.builder().username("misc.k.h").password("daniel2005h").build();
        instagram.setup();

        try{
            instagram.login();
            System.out.println("Logged in...");
        } catch (Exception e){
            e.printStackTrace();
        }

        List<String> recipients = new ArrayList<String>();
        InstagramInboxResult inbox = instagram.sendRequest(new InstagramGetInboxRequest());
        for (InstagramInboxThread thread : inbox.getInbox().getThreads()){
            for (InstagramInboxThreadItem threadItem : thread.getItems()){
                InstagramSearchUsernameResult user = instagram.sendRequest(new InstagramSearchUsernameRequest(threadItem.getUser_id()));
                recipients.add(String.valueOf(user.getUser().getPk()));
            }
        }

        instagram.sendRequest(InstagramDirectShareRequest.builder().shareType(ShareType.MESSAGE).recipients(recipients).message("test").build());
    }
}
