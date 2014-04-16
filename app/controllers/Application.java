package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Comment;
import models.Music;
import models.User;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
}