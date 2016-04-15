import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import twitter4j.conf.*; 
import twitter4j.*; 
import twitter4j.auth.*; 
import twitter4j.api.*; 
import java.util.*; 
import java.text.SimpleDateFormat; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CoCAtwitterfeed extends PApplet {

//////////////////////////
// CoCATwitterFeed processing sketch
// o.blair@massey.ac.nz
// t.turnidge@massey.ac.nz
//////////////////////////

//////////////////////////
// CoCATwitterFeed displays tweets and tweet photos dynamically sized to the screen.
// Intended for use around Massey College of Creative Arts during 2016
//////////////////////////

// LOAD LIBRARIES







Twitter twitter;
PFont f;
PImage webImg;


// HASHTAGS TO PULL TWEETS FROM
String searchString = "#makethespace OR #coca158 OR #coca379 OR #tweetmyproject OR #spatial3 OR #spatial4 OR #spatialtype OR #designexpedition OR #coca1 OR #coca2 OR #coca3 OR #coca4 OR #coca494 OR #coca454";
//String searchString = "#spatialtype";

List<Status> tweets;

int currentTweet;
int currentTime;
int tweetTime = 12000;
int tweetCount = 99;
int padding = 20; // screen/canvas padding
int hilightColor = color(0, 176, 237);



public void setup()
{
    // get system font list (for testing)
    //printArray(PFont.list());
    
    // screen size (for testing)
    
    //size(540,960);
    surface.setResizable(true);
    
    // SET TWITTER OAUTH KEYS (uses @fractaldesign account)
    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setOAuthConsumerKey("3wrRp2bRuPj2GuoAmZjW21fSC");
    cb.setOAuthConsumerSecret("3yO9mQCpFOm59JmOIXA11cxv4n3oEuMrNhFXcJdZEQAHw77pGu");
    cb.setOAuthAccessToken("18865530-7aKmrzg2bK9TZtmxVdjfJABnPHhhVJBXpityLeg6Y");
    cb.setOAuthAccessTokenSecret("zQwrLEV8INBSWqjemRhEPLe87vKFKFXJjbFSE6rsarGOo");

    TwitterFactory tf = new TwitterFactory(cb.build());
    twitter = tf.getInstance();
    
    getNewTweets();
    
    currentTweet = tweets.size() - 1;
    currentTime = millis() - tweetTime;

}

public void draw()
{
    //fill(0, 1);
    //rect(0, 0, width, height);

    if (currentTime + tweetTime < millis()) {
      
      //clear the screen?
      background(0, 176, 237);
      //fill(0, 120);
      //rect(0, 0, width, height);
   

      Status status = tweets.get(currentTweet);
 
 
      blendMode(BLEND);


      // IMAGE   
      for(MediaEntity m : status.getMediaEntities()){ //search trough your entities
        webImg = loadImage(m.getMediaURL());
      }
      
      // QUOTED IMAGE   
      if (status.getQuotedStatus() != null) {
        for(MediaEntity m : status.getQuotedStatus().getMediaEntities()){ //search trough your entities
          webImg = loadImage(m.getMediaURL());
        }
      }
      

      if (webImg != null) {
        background(0);
        
        // FULL BLEED IMAGE BACKGROUND
        //webImg.resize(width, 0);
        //if (webImg.height < height){webImg.resize(0, height);}
        //image(webImg, -(webImg.width-width)/2, 0);
        
        // SIMPLE STRETCHED IMAGE BACKGROUND
        //webImg.resize(width, 0);
        //if (webImg.height > height){webImg.resize(0, height);}
        //image(webImg, -(webImg.width-width)/2, -(webImg.height-height)/2);
        
        // COMPLEX STRETCHED IMAGE BACKGROUND
        // this will resize the images to fullscreen if they are a little smaller than fullscreen.
        webImg.resize(width, 0);
        if (webImg.height > height*0.7f && webImg.height < height){webImg.resize(0, height);}
        if (webImg.height > height*1.3f){webImg.resize(0, height);}
        image(webImg, -(webImg.width-width)/2, -(webImg.height-height)/2);
        
      }
      
 
 
      // tweet group random positioning
      float tweetWidth = width*0.7f;
      float tweetHeight = width*0.4f;
      
      if (width > height) {
        tweetWidth = height*0.7f;
        tweetHeight = height*0.4f;
      }
      
      float tweetX = 0;
      float tweetY = 0;

      // minimise the range the tweet text can be in if we have an image, so as not to block the image...
      if (webImg != null){
        if (Math.random() >= 0.5f){
          tweetX = padding*2;
        } else {
          tweetX = width-tweetWidth-padding-padding;
        }
        if (Math.random() >= 0.5f){
          tweetY = padding*2;
        } else {
          tweetY = height-padding-padding-tweetHeight;
        } 
      }
      else {
        tweetX = random(padding*2, width-tweetWidth-padding-padding);
        tweetY = random(padding*2, height-padding-padding-tweetHeight);
      }
      
      float tweetFontSize = (width+height)/70;
      
      // BOX
      fill(0, (255*0.85f));
      rect(tweetX-padding, tweetY-padding, tweetWidth+(padding*2), tweetHeight+(padding*2));
      
  
      // DATE
      //String df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      //String tweetDate = df.format(status.getCreatedAt());
      //String tweetDate = status.getCreatedAt();
      Calendar now = Calendar.getInstance();
      Calendar tweetDate = Calendar.getInstance();
      tweetDate.setTime(status.getCreatedAt());
  
      long diff = now.getTimeInMillis() - tweetDate.getTimeInMillis();
      //long diffSeconds = diff / 1000 % 60;
      long diffMinutes = diff / (60 * 1000) % 60;
      long diffHours = diff / (60 * 60 * 1000) % 24;
      long diffDays = diff / (24 * 60 * 60 * 1000);
  
      String timeStamp;
  
  //    if (diffDays < 1) {
  //      // posted < 24 hours ago
  //      if (diffHours > 1) {
  //        timeStamp = (diffHours + 1) + " hours ago";
  //      } else if (diffHours == 1) {
  //        if (diffMinutes >= 30) {
  //          timeStamp = (diffHours + 1) + " hours ago";
  //        } else {
  //          timeStamp = "about an hour ago";
  //        }
  //      } else {
  //        // posted < 1 hour ago
  //        if (diffMinutes > 1) {
  //          timeStamp = (diffMinutes + 1) + " minutes ago";
  //        } else if (diffMinutes == 1) {  
  //          timeStamp = "about a minute ago";
  //        } else {
  //          timeStamp = "less than a minute ago";
  //        }
  //      }
  //    } else {
  //      // posted over a day ago, show date
  //      timeStamp = new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(tweetDate.getTime());
  //    }
 
      // TODAY or YESTERDAY
      // figure out what day yesterday was
      Calendar yester = Calendar.getInstance();
      yester.add(Calendar.DATE, -1);

      String toDay = "";
      String yesterDay = "";
      String tweetDay = "";
      String dayStamp = "";
      
      toDay = new SimpleDateFormat("dd").format(now.getTime());
      yesterDay = new SimpleDateFormat("dd").format(yester.getTime());
      tweetDay = new SimpleDateFormat("dd").format(tweetDate.getTime());

      println(toDay + " " + yesterDay + " " + tweetDay);

      // today
      if (tweetDay.equals(toDay) == true) {
        println("today");
        dayStamp = "today";
        timeStamp = new SimpleDateFormat("h:mm a").format(tweetDate.getTime());
        
      // yesterday
      } else if (tweetDay.equals(yesterDay) == true) {
        println("yesterday");
        dayStamp = "yesterday";
        timeStamp = new SimpleDateFormat("h:mm a").format(tweetDate.getTime());
        
      // older
      } else {
        println("older");
        timeStamp = new SimpleDateFormat("d/M/yyyy h:mm a").format(tweetDate.getTime());
     }
      
      timeStamp = timeStamp + " " + dayStamp;

  
      //println(diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes, " + diffSeconds + " seconds.");
      //println(timeStamp);
  
      fill(255);
      f = loadFont("ThreeSix10-072Regular-48.vlw");
      textFont(f, tweetFontSize);
      textLeading(tweetFontSize);
      text(" " + timeStamp, tweetX, tweetY+tweetHeight);
  
  
  
      // AUTHOR
      String tweetAuthor = status.getUser().getName() + "\n@" + status.getUser().getScreenName();
      fill(hilightColor);
      f = loadFont("ThreeSix10-126Heavy-48.vlw");
      textFont(f, tweetFontSize);
      textLeading(tweetFontSize);
      text(tweetAuthor, tweetX, tweetY+tweetHeight-(tweetFontSize*2));
 
 
      // TWEET
      String tweetText = "";
      
      // for Quoted tweets, remove the link to the quoted tweet, and expand any URLS in the quoted tweet.
      if (status.getQuotedStatus() != null) {
        
        String quotedURLS = "";
        for (URLEntity urle : status.getQuotedStatus().getURLEntities()) {
          quotedURLS = (urle.getExpandedURL());
        }
        
        tweetText = status.getText().replaceAll("(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)",
        "$1 $4") + "\n\n" + status.getQuotedStatus().getText().replaceAll("(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)",
        "$1" + quotedURLS + "$4");
        
      
      // for Regular tweets, expand any URLS.
      } else {
        
        String tweetURLS = "";
        for (URLEntity urle : status.getURLEntities()) {
          tweetURLS = (urle.getExpandedURL());
        }
        tweetText = status.getText().replaceAll("(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)",
        "$1" + tweetURLS + "$4");
        
      }

      fill(255);
      f = loadFont("ThreeSix10-072Regular-48.vlw");
      textFont(f, tweetFontSize);
      textLeading(tweetFontSize);
      text(tweetText, tweetX, tweetY, tweetWidth, tweetHeight-tweetFontSize-tweetFontSize);
   
   
      if (webImg != null) {
        // reset the image
        webImg = null;
      }

   
      currentTweet = currentTweet - 1;
      currentTime = millis();
 
     
      if (currentTweet <= 0)
      {
          getNewTweets();
          currentTweet = tweets.size() - 1;
          
      }
    }
}



// LOAD TWEETS
public void getNewTweets()
{
    try
    {
        Query query = new Query(searchString);
        query.setCount(tweetCount);
        QueryResult result = twitter.search(query);
        tweets = result.getTweets();
    }
    catch (TwitterException te)
    {
        System.out.println("Failed to search tweets: " + te.getMessage());
        System.exit(-1);
    }
}



// CLICK FOR NEXT
public void mouseClicked() {
  currentTime = millis() - tweetTime;
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#00B0ED", "--hide-stop", "CoCAtwitterfeed" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
