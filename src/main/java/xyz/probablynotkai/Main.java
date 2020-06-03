package xyz.probablynotkai;

import org.apache.http.HttpHost;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.*;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest.ShareType;
import org.brunocvcunha.instagram4j.requests.payload.*;

import java.io.File;
import java.util.*;

public class Main
{
    @lombok.SneakyThrows
    public static void main(String[] args) {
        UI ui = new UI();
        ui.runGUI();
    }
}
