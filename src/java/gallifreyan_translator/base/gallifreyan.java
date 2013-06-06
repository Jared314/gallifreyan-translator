// Gallifreyan Translator v0.3.0
// Loren Sherman
// http://blackhatguy.deviantart.com/art/Gallifreyan-Translator-280910144

package gallifreyan_translator.base;

import processing.core.*;
import processing.xml.*;

import java.applet.*;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;
import java.awt.Image;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import java.util.regex.*;

public class gallifreyan extends PApplet {

  int bg = color(255);
  protected int getBg(){ return this.bg; }
  protected void setBg(int value){ this.bg = value; }

  int fg = color(0);
  protected int getFg(){ return this.fg; }
  protected void setFg(int value){ this.fg = value; }

  String english = "Enter text here and press return.";
  protected String getEnglish(){ return this.english; }
  protected void setEnglish(String value){ this.english = value; }

  float count = 0;
  protected float getCount(){ return this.count; }
  protected void setCount(float value){ this.count = value; }

  float sentenceRadius = 256;
  protected float getSentenceRadius(){ return this.sentenceRadius; }

  protected boolean getKeyPressed(){ return this.keyPressed; }
  protected int getKeyCode(){ return this.keyCode; }
  protected char getKey(){ return this.key; }

// public void setup() {
//   smooth();
//   size(1024, 600);
//   background(bg);
//   PFont font = loadFont("Futura-Medium-15.vlw");
//   fill(fg);
//   textFont(font);
//   text(english,15,30);
//   stroke(fg);
//   strokeWeight(1);
//   noFill();
//   frameRate(30);
// }

// public void draw(){
//   if(keyPressed&&keyCode==CONTROL){
//     transliterate(english, fg, bg);
//     text(english,15,30);
//     count+=0.02f;
//   }
// }

// public void keyPressed(){
//   if(english=="Enter text here and press return."&&keyCode!=SHIFT){
//     english=""+key;
//     background(bg);
//     text(english,15,30);
//   }else if (keyCode==SHIFT){
//   }else if (keyCode==CONTROL){
//   }else if (keyCode==TAB){
//     stroke(bg);
//     strokeWeight(400);
//     ellipse(width/2,height/2,(sentenceRadius+222)*2,(sentenceRadius+222)*2);
//     saveFrame(english+" ####.png");
//     text("Your image has been saved to the",15,30);
//     text("folder that contains this program.",15,50);
//   }else if (keyCode==ALT){
//     bg=color(random(255),random(255),random(255));
//     fg=color(random(255),random(255),random(255));
//     transliterate(english, fg, bg);
//     text(english,15,30);
//   }else if (keyCode==DELETE||keyCode==BACKSPACE){
//     String oldenglish=english;
//     english="";
//     for(int n=0;n<oldenglish.length()-1;n++){
//       english=english+oldenglish.charAt(n);
//     }
//     background(bg);
//     text(english,15,30);
//   }else if (keyCode==RETURN||keyCode==ENTER){
//     transliterate(english, fg, bg);
//     text(english,15,30);
//   }else{
//     english=english+key;
//     background(bg);
//     text(english,15,30);
//   }
// }



public void transliterate(String english, int fg, int bg){
  english=english.toLowerCase();
  english=join(split(english, " -"), "-");
  english=join(split(english, "- "), "-");
  english=join(split(english, "-"), "- ");
  english=join(split(english, "ch"), "#");
  english=join(split(english, "sh"), "$");
  english=join(split(english, "th"), "%");
  english=join(split(english, "ng"), "&");
  english=join(split(english, "qu"), "q");
  background(bg);
  int spaces=0;
  int sentences=1;
  for (int i=0;i<english.length();i++) {

    if (english.charAt(i)=='c') {
      text("ERROR: Please replace every C with a K or an S.",15,60);
      return;
    }
    if (english.charAt(i)==' ') {
      spaces++;
    }
    if ((english.charAt(i)=='.'||english.charAt(i)=='!'||english.charAt(i)=='?')&&i<english.length()-1) {
      if (english.charAt(i+1)==' ') {
        sentences++;
      }
    }
  }
  if (spaces==0) {
    writeSentence(0, english);
  }
  else if (sentences==1) {
    writeSentence(1, english);
  }else{
    text("ERROR: Multiple sentences are not yet supported.",15,60);
    return;
  }
  text("Press return again for another version.",15,60);
  text("Hold control to animate.",15,120);
  text("Press alt to randomize colors.",15,90);
  text("Press tab to save image.",15,150);
}

public void writeSentence(int type, String english) {
  float[] wordRadius = {};
  float1=0;
  float2=0;
  float charCount=0;
  if(english.charAt(english.length()-1)==' '){
    String oldenglish=english;
    english="";
    for(int n=0;n<oldenglish.length()-1;n++){
      english=english+oldenglish.charAt(n);
    }
  }
  String Word="";
  String[] Sentence= {
  };
  Sentence=split(english, " ");
  String[][] sentence = new String[Sentence.length][];
  char[] punctuation = new char[Sentence.length];
  boolean[][] apostrophes = new boolean[Sentence.length][100];
  for (int j=0;j<Sentence.length;j++) {
    String[] word= {
    };
    Sentence[j]=join(split(Sentence[j], " "), "");
    boolean vowel=true;
    for (int i=0;i<Sentence[j].length();i++) {
      if (i!=0) {
        if (Sentence[j].charAt(i)==Sentence[j].charAt(i-1)) {
          word[word.length-1]=word[word.length-1]+'@';
          continue;
        }
      }
      if (Sentence[j].charAt(i)=='a'||Sentence[j].charAt(i)=='e'||Sentence[j].charAt(i)=='i'||Sentence[j].charAt(i)=='o'||Sentence[j].charAt(i)=='u') {
        if (vowel) {
          word=append(word, str(Sentence[j].charAt(i)));
        }
        else {
          word[word.length-1]=word[word.length-1]+Sentence[j].charAt(i);
        }
        vowel=true;
      }
      else if (Sentence[j].charAt(i)=='.'||Sentence[j].charAt(i)=='?'||Sentence[j].charAt(i)=='!'||Sentence[j].charAt(i)=='"'||Sentence[j].charAt(i)=="'".charAt(0)||Sentence[j].charAt(i)=='-'||Sentence[j].charAt(i)==','||Sentence[j].charAt(i)==';'||Sentence[j].charAt(i)==':') {
        if(Sentence[j].charAt(i)=="'".charAt(0)){
          apostrophes[j][i]=true;
        }else{
          punctuation[j]=Sentence[j].charAt(i);
        }
      }
      else {
        word=append(word, str(Sentence[j].charAt(i)));
        if (Sentence[j].charAt(i)=='t'||Sentence[j].charAt(i)=='$'||Sentence[j].charAt(i)=='r'||Sentence[j].charAt(i)=='s'||Sentence[j].charAt(i)=='v'||Sentence[j].charAt(i)=='w') {
          vowel=true;
        }
        else {
          vowel=false;
        }
      }
    }
    sentence[j]=word;
    charCount+=word.length;
  }
  stroke(fg);
  if (type>0) {
    strokeWeight(3);
    ellipse(width/2, height/2, sentenceRadius*2, sentenceRadius*2);
  }
  strokeWeight(4);
  ellipse(width/2, height/2, sentenceRadius*2+40, sentenceRadius*2+40);
  float pos=PI/2;
  float maxRadius=0;
  for (int i=0;i<sentence.length;i++) {
    wordRadius=append(wordRadius, constrain(sentenceRadius*sentence[i].length/charCount*1.2f, 0, sentenceRadius/2));
    if (wordRadius[i]>maxRadius) {
      maxRadius=wordRadius[i];
    }
  }
  float scaleFactor = sentenceRadius/(maxRadius+(sentenceRadius/2));
  float distance=scaleFactor*sentenceRadius/2;
  for (int i=0;i<wordRadius.length;i++) {
    wordRadius[i]*=scaleFactor;
  }
  float[] x= {
  };
  float[] y= {
  };
  stroke(fg);
  for (int i=0;i<sentence.length;i++) {
    x=append(x, width/2+distance*cos(pos));
    y=append(y, height/2+distance*sin(pos));
    int nextIndex=0;
    if (i!=sentence.length-1) {
      nextIndex=i+1;
    }
    pos-=PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*TWO_PI;
    float pX = width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius;
    float pY = height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius;
    switch(punctuation[i]){
      case '.':
        ellipse(pX,pY,20,20);
        break;
      case '?':
        makeDots(width/2,height/2,sentenceRadius*1.4f,2,-1.2f,0.1f);
        break;
      case '!':
        makeDots(width/2,height/2,sentenceRadius*1.4f,3,-1.2f,0.1f);
        break;
      case '"':
        line(width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20),height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20));
        break;
      case '-':
        line(width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20),height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20));
        line(width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length+0.3f)/(2*charCount)*PI)*sentenceRadius,height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length+0.2f)/(2*charCount)*PI)*sentenceRadius,width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length+0.2f)/(2*charCount)*PI)*(sentenceRadius+20),height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length+0.3f)/(2*charCount)*PI)*(sentenceRadius+20));
        line(width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length-0.3f)/(2*charCount)*PI)*sentenceRadius,height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length-0.2f)/(2*charCount)*PI)*sentenceRadius,width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length-0.2f)/(2*charCount)*PI)*(sentenceRadius+20),height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length-0.3f)/(2*charCount)*PI)*(sentenceRadius+20));
        break;
      case ',':
        fill(fg);
        ellipse(pX,pY,20,20);
        noFill();
        break;
      case ';':
        fill(fg);
        ellipse(width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius-10,height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius-10,10,10);
        noFill();
        break;
      case ':':
        ellipse(pX,pY,25,25);
        strokeWeight(2);
        ellipse(pX,pY,15,15);
        strokeWeight(4);
        break;
      default:
        break;
    }
  }
  int otherIndex=0;
  boolean[][] nested = new boolean[sentence.length][100];
  for (int i=0;i<sentence.length;i++) {
    float angle1=0;//angle facing onwards
    float angle2=0;//backwards
    if (i==sentence.length-1) {
      otherIndex=0;
    }
    else {
      otherIndex=i+1;
    }
    angle1=atan((y[i]-y[otherIndex])/(x[i]-x[otherIndex]));
    if (dist(x[i]+(cos(angle1)*20), y[i]+(sin(angle1)*20), x[otherIndex], y[otherIndex])>dist(x[i], y[i], x[otherIndex], y[otherIndex])) {
      angle1-=PI;
    }
    if (angle1<0) {
      angle1+=TWO_PI;
    }
    if (angle1<0) {
      angle1+=TWO_PI;
    }
    angle1-=PI/2;
    if (angle1<0) {
      angle1+=TWO_PI;
    }
    angle1=TWO_PI-angle1;
    int index = round(map(angle1, 0, TWO_PI, 0, PApplet.parseFloat(sentence[i].length)));
    if (index==sentence[i].length) {
      index=0;
    }
    char tempChar=sentence[i][index].charAt(0);
    if ((tempChar=='t'||tempChar=='$'||tempChar=='r'||tempChar=='s'||tempChar=='v'||tempChar=='w')&&type>0) {
      nested[i][index]=true;
      wordRadius[i]=constrain(wordRadius[i]*1.2f, 0, maxRadius*scaleFactor);
      while (dist (x[i], y[i], x[otherIndex], y[otherIndex])>wordRadius[i]+wordRadius[otherIndex]) {
        x[i]=lerp(x[i], x[otherIndex], 0.05f);
        y[i]=lerp(y[i], y[otherIndex], 0.05f);
      }
    }
  }
  float[] lineX = {
  };
  float[] lineY = {
  };
  float[] arcBegin = {
  };
  float[] arcEnd = {
  };
  float[] lineRad = {
  };
  strokeWeight(2);
  if (type==0) {
    wordRadius[0]=sentenceRadius*0.9f;
    x[0]=width/2;
    y[0]=height/2;
  }
  for (int i=0;i<sentence.length;i++) {
    pos=PI/2;
    float letterRadius = wordRadius[i]/(sentence[i].length+1)*1.5f;
    for (int j=0;j<sentence[i].length;j++) {
      if(apostrophes[i][j]){
        float a=pos+PI/sentence[i].length-0.1f;
        float d=0;
        float tempX=x[i];
        float tempY=y[i];
        while (pow (tempX-width/2, 2)+pow(tempY-height/2, 2)<pow(sentenceRadius+20, 2)) {
          tempX=x[i]+cos(a)*d;
          tempY=y[i]+sin(a)*d;
          d+=1;
        }
        line(x[i]+cos(a)*wordRadius[i], y[i]+sin(a)*wordRadius[i], tempX, tempY);
        a=pos+PI/sentence[i].length+0.1f;
        d=0;
        tempX=x[i];
        tempY=y[i];
        while (pow (tempX-width/2, 2)+pow(tempY-height/2, 2)<pow(sentenceRadius+20, 2)) {
          tempX=x[i]+cos(a)*d;
          tempY=y[i]+sin(a)*d;
          d+=1;
        }
        line(x[i]+cos(a)*wordRadius[i], y[i]+sin(a)*wordRadius[i], tempX, tempY);
      }
      boolean vowel=true;
      float tempX=0;
      float tempY=0;

      //single vowels

      if (sentence[i][j].charAt(0)=='a') {
        tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
        tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
        ellipse(tempX, tempY, letterRadius, letterRadius);
      }
      else if (sentence[i][j].charAt(0)=='e') {
        tempX=x[i]+cos(pos)*(wordRadius[i]);
        tempY=y[i]+sin(pos)*(wordRadius[i]);
        ellipse(tempX, tempY, letterRadius, letterRadius);
      }
      else if (sentence[i][j].charAt(0)=='i') {
        tempX=x[i]+cos(pos)*(wordRadius[i]);
        tempY=y[i]+sin(pos)*(wordRadius[i]);
        ellipse(tempX, tempY, letterRadius, letterRadius);
        lineX=append(lineX, tempX);
        lineY=append(lineY, tempY);
        arcBegin=append(arcBegin, pos+PI/2);
        arcEnd=append(arcEnd, pos+3*PI/2);
        lineRad=append(lineRad, letterRadius);
      }
      else if (sentence[i][j].charAt(0)=='o') {
        tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius/1.6f);
        tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius/1.6f);
        ellipse(tempX, tempY, letterRadius, letterRadius);
      }
      else if (sentence[i][j].charAt(0)=='u') {
        tempX=x[i]+cos(pos)*(wordRadius[i]);
        tempY=y[i]+sin(pos)*(wordRadius[i]);
        ellipse(tempX, tempY, letterRadius, letterRadius);
        lineX=append(lineX, tempX);
        lineY=append(lineY, tempY);
        arcBegin=append(arcBegin, pos-PI/2);
        arcEnd=append(arcEnd, pos+PI/2);
        lineRad=append(lineRad, letterRadius);
      }
      else {
        vowel=false;
      }

      if (vowel) {
        arc(x[i], y[i], wordRadius[i]*2, wordRadius[i]*2, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
        if (sentence[i][j].length()==1) {
        }
        else {

          //double vowels

          if (sentence[i][j].charAt(1)=='@') {
            ellipse(tempX, tempY, letterRadius*1.3f, letterRadius*1.3f);
          }
        }
      }
      else {

        // consonants

        if (sentence[i][j].charAt(0)=='b'||sentence[i][j].charAt(0)=='#'||sentence[i][j].charAt(0)=='d'||sentence[i][j].charAt(0)=='f'||sentence[i][j].charAt(0)=='g'||sentence[i][j].charAt(0)=='h') {
          tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius*0.95f);
          tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius*0.95f);
          makeArcs(tempX, tempY, x[i], y[i], wordRadius[i], letterRadius, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
          int lines=0;
          if (sentence[i][j].charAt(0)=='#') {
            makeDots(tempX, tempY, letterRadius, 2, pos, 1);
          }
          else if (sentence[i][j].charAt(0)=='d') {
            makeDots(tempX, tempY, letterRadius, 3, pos, 1);
          }
          else if (sentence[i][j].charAt(0)=='f') {
            lines=3;
          }
          else if (sentence[i][j].charAt(0)=='g') {
            lines=1;
          }
          else if (sentence[i][j].charAt(0)=='h') {
            lines=2;
          }
          for (int k=0;k<lines;k++) {
            lineX=append(lineX, tempX);
            lineY=append(lineY, tempY);
            arcBegin=append(arcBegin, pos+0.5f);
            arcEnd=append(arcEnd, pos+TWO_PI-0.5f);
            lineRad=append(lineRad, letterRadius*2);
          }
          if (sentence[i][j].length()>1) {
            int vowelIndex=1;
            if (sentence[i][j].charAt(1)=='@') {
              makeArcs(tempX, tempY, x[i], y[i], wordRadius[i], letterRadius*1.3f, pos+TWO_PI, pos-TWO_PI);
              vowelIndex=2;
            }
            if (sentence[i][j].length()==vowelIndex) {
              pos-=TWO_PI/sentence[i].length;
              continue;
            }
            if (sentence[i][j].charAt(vowelIndex)=='a') {
              tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
              tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='e') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='i') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
              lineX=append(lineX, tempX);
              lineY=append(lineY, tempY);
              arcBegin=append(arcBegin, pos+PI/2);
              arcEnd=append(arcEnd, pos+3*PI/2);
              lineRad=append(lineRad, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='o') {
              tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius*2);
              tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius*2);
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='u') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
              lineX=append(lineX, tempX);
              lineY=append(lineY, tempY);
              arcBegin=append(arcBegin, pos-PI/2);
              arcEnd=append(arcEnd, pos+PI/2);
              lineRad=append(lineRad, letterRadius);
            }
            if (sentence[i][j].length()==(vowelIndex+2)) {
              if (sentence[i][j].charAt(vowelIndex+1)=='@') {
                ellipse(tempX, tempY, letterRadius*1.3f, letterRadius*1.3f);
              }
            }
          }
        }
        if (sentence[i][j].charAt(0)=='j'||sentence[i][j].charAt(0)=='k'||sentence[i][j].charAt(0)=='l'||sentence[i][j].charAt(0)=='m'||sentence[i][j].charAt(0)=='n'||sentence[i][j].charAt(0)=='p') {
          tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius);
          tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius);
          ellipse(tempX, tempY, letterRadius*1.9f, letterRadius*1.9f);
          arc(x[i], y[i], wordRadius[i]*2, wordRadius[i]*2, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
          int lines=0;
          if (sentence[i][j].charAt(0)=='k') {
            makeDots(tempX, tempY, letterRadius, 2, pos, 1);
          }
          else if (sentence[i][j].charAt(0)=='l') {
            makeDots(tempX, tempY, letterRadius, 3, pos, 1);
          }
          else if (sentence[i][j].charAt(0)=='m') {
            lines=3;
          }
          else if (sentence[i][j].charAt(0)=='n') {
            lines=1;
          }
          else if (sentence[i][j].charAt(0)=='p') {
            lines=2;
          }
          for (int k=0;k<lines;k++) {
            lineX=append(lineX, tempX);
            lineY=append(lineY, tempY);
            arcBegin=append(arcBegin, 0);
            arcEnd=append(arcEnd, TWO_PI);
            lineRad=append(lineRad, letterRadius*1.9f);
          }
          if (sentence[i][j].length()>1) {
            int vowelIndex=1;
            if (sentence[i][j].charAt(1)=='@') {
              ellipse(tempX, tempY, letterRadius*2.3f, letterRadius*2.3f);
              vowelIndex=2;
            }
            if (sentence[i][j].length()==vowelIndex) {
              pos-=TWO_PI/sentence[i].length;
              continue;
            }
            if (sentence[i][j].charAt(vowelIndex)=='a') {
              tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
              tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='e') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='i') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
              lineX=append(lineX, tempX);
              lineY=append(lineY, tempY);
              arcBegin=append(arcBegin, pos+PI/2);
              arcEnd=append(arcEnd, pos+3*PI/2);
              lineRad=append(lineRad, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='o') {
              tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius*2);
              tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius*2);
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='u') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
              lineX=append(lineX, tempX);
              lineY=append(lineY, tempY);
              arcBegin=append(arcBegin, pos-PI/2);
              arcEnd=append(arcEnd, pos+PI/2);
              lineRad=append(lineRad, letterRadius);
            }
            if (sentence[i][j].length()==(vowelIndex+2)) {
              if (sentence[i][j].charAt(vowelIndex+1)=='@') {
                ellipse(tempX, tempY, letterRadius*1.3f, letterRadius*1.3f);
              }
            }
          }
        }
        if (sentence[i][j].charAt(0)=='t'||sentence[i][j].charAt(0)=='$'||sentence[i][j].charAt(0)=='r'||sentence[i][j].charAt(0)=='s'||sentence[i][j].charAt(0)=='v'||sentence[i][j].charAt(0)=='w') {
          tempX=x[i]+cos(pos)*(wordRadius[i]);
          tempY=y[i]+sin(pos)*(wordRadius[i]);
          int nextIndex;
          if (i==sentence.length-1) {
            nextIndex=0;
          }
          else {
            nextIndex=i+1;
          }
          float angle1=atan((y[i]-y[nextIndex])/(x[i]-x[nextIndex]));
          if (dist(x[i]+(cos(angle1)*20), y[i]+(sin(angle1)*20), x[nextIndex], y[nextIndex])>dist(x[i], y[i], x[nextIndex], y[nextIndex])) {
            angle1-=PI;
          }
          if (angle1<0) {
            angle1+=TWO_PI;
          }
          if (angle1<0) {
            angle1+=TWO_PI;
          }
          if (nested[i][j]) {
            makeArcs(x[nextIndex], y[nextIndex], x[i], y[i], wordRadius[i], wordRadius[nextIndex]+20, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
          }
          else {
            makeArcs(tempX, tempY, x[i], y[i], wordRadius[i], letterRadius*1.5f, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
          }
          int lines=0;
          if (sentence[i][j].charAt(0)=='$') {
            if (nested[i][j]) {
              makeDots(x[nextIndex], y[nextIndex], (wordRadius[nextIndex]*1.4f)+14, 2, angle1, wordRadius[nextIndex]/500);
            }
            else {
              makeDots(tempX, tempY, letterRadius*2.6f, 2, pos, 0.5f);
            }
          }
          else if (sentence[i][j].charAt(0)=='r') {
            if (nested[i][j]) {
              makeDots(x[nextIndex], y[nextIndex], (wordRadius[nextIndex]*1.4f)+14, 3, angle1, wordRadius[nextIndex]/500);
            }
            else {
              makeDots(tempX, tempY, letterRadius*2.6f, 3, pos, 0.5f);
            }
          }
          else if (sentence[i][j].charAt(0)=='s') {
            lines=3;
          }
          else if (sentence[i][j].charAt(0)=='v') {
            lines=1;
          }
          else if (sentence[i][j].charAt(0)=='w') {
            lines=2;
          }
          if (nested[i][j]) {
            for (int k=0;k<lines;k++) {
              lineX=append(lineX, x[nextIndex]);
              lineY=append(lineY, y[nextIndex]);
              arcBegin=append(arcBegin, float1);
              arcEnd=append(arcEnd, float2);
              lineRad=append(lineRad, wordRadius[nextIndex]*2+40);
            }
          }
          else {
            for (int k=0;k<lines;k++) {
              lineX=append(lineX, tempX);
              lineY=append(lineY, tempY);
              arcBegin=append(arcBegin, float1);
              arcEnd=append(arcEnd, float2);
              lineRad=append(lineRad, letterRadius*3);
            }
          }
          if (sentence[i][j].length()>1) {
            if (sentence[i][j].charAt(1)=='@') {
              if (nested[i][j]) {
                makeArcs(x[nextIndex], y[nextIndex], x[i], y[i], wordRadius[i], (wordRadius[nextIndex]+20)*1.2f, pos+TWO_PI, pos-TWO_PI);
              }
              else {
                makeArcs(tempX, tempY, x[i], y[i], wordRadius[i], letterRadius*1.8f, pos+TWO_PI, pos-TWO_PI);
              }
            }
          }
        }
        if (sentence[i][j].charAt(0)=='%'||sentence[i][j].charAt(0)=='y'||sentence[i][j].charAt(0)=='z'||sentence[i][j].charAt(0)=='&'||sentence[i][j].charAt(0)=='q'||sentence[i][j].charAt(0)=='x') {
          tempX=x[i]+cos(pos)*(wordRadius[i]);
          tempY=y[i]+sin(pos)*(wordRadius[i]);
          ellipse(tempX, tempY, letterRadius*2, letterRadius*2);
          arc(x[i], y[i], wordRadius[i]*2, wordRadius[i]*2, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
          int lines=0;
          if (sentence[i][j].charAt(0)=='y') {
            makeDots(tempX, tempY, letterRadius, 2, pos, 1);
          }
          else if (sentence[i][j].charAt(0)=='z') {
            makeDots(tempX, tempY, letterRadius, 3, pos, 1);
          }
          else if (sentence[i][j].charAt(0)=='&') {
            lines=3;
          }
          else if (sentence[i][j].charAt(0)=='q') {
            lines=1;
          }
          else if (sentence[i][j].charAt(0)=='x') {
            lines=2;
          }
          for (int k=0;k<lines;k++) {
            lineX=append(lineX, tempX);
            lineY=append(lineY, tempY);
            arcBegin=append(arcBegin, 0);
            arcEnd=append(arcEnd, TWO_PI);
            lineRad=append(lineRad, letterRadius*2);
          }
          if (sentence[i][j].length()>1) {
            int vowelIndex=1;
            if (sentence[i][j].charAt(1)=='@') {
              ellipse(tempX, tempY, letterRadius*2.3f, letterRadius*2.3f);
              vowelIndex=2;
            }
            if (sentence[i][j].length()==vowelIndex) {
              pos-=TWO_PI/sentence[i].length;
              continue;
            }
            if (sentence[i][j].charAt(vowelIndex)=='a') {
              tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
              tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='e') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='i') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
              lineX=append(lineX, tempX);
              lineY=append(lineY, tempY);
              arcBegin=append(arcBegin, pos+PI/2);
              arcEnd=append(arcEnd, pos+3*PI/2);
              lineRad=append(lineRad, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='o') {
              tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius);
              tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius);
              ellipse(tempX, tempY, letterRadius, letterRadius);
            }
            else if (sentence[i][j].charAt(vowelIndex)=='u') {
              ellipse(tempX, tempY, letterRadius, letterRadius);
              lineX=append(lineX, tempX);
              lineY=append(lineY, tempY);
              arcBegin=append(arcBegin, pos-PI/2);
              arcEnd=append(arcEnd, pos+PI/2);
              lineRad=append(lineRad, letterRadius);
            }
            if (sentence[i][j].length()==(vowelIndex+2)) {
              if (sentence[i][j].charAt(vowelIndex+1)=='@') {
                ellipse(tempX, tempY, letterRadius*1.8f, letterRadius*1.8f);
              }
            }
          }
        }
      }
      pos-=TWO_PI/sentence[i].length;
    }
  }
  strokeWeight(2);
  float[] lineLengths= {
  };
  stroke(fg);
  for (int i=0;i<lineX.length;i++) {
    int[] indexes= {
    };
    float[]angles= {
    };
    for (int j=0;j<lineX.length;j++) {
      if (round(lineY[i])==round(lineY[j])&&round(lineX[i])==round(lineX[j])) {
        continue;
      }
      boolean b=false;
      for (int k=0;k<lineLengths.length;k++) {
        if (lineLengths[k]==dist(lineX[i], lineY[i], lineX[j], lineY[j])+lineX[i]+lineY[i]+lineX[j]+lineY[j]) {
          b=true;
          break;
        }
      }
      if (b) {
        continue;
      }
      float angle1=atan((lineY[i]-lineY[j])/(lineX[i]-lineX[j]));
      if (dist(lineX[i]+(cos(angle1)*20), lineY[i]+(sin(angle1)*20), lineX[j], lineY[j])>dist(lineX[i], lineY[i], lineX[j], lineY[j])) {
        angle1-=PI;
      }
      if (angle1<0) {
        angle1+=TWO_PI;
      }
      if (angle1<0) {
        angle1+=TWO_PI;
      }
      if (angle1<arcEnd[i]&&angle1>arcBegin[i]) {
        angle1-=PI;
        if (angle1<0) {
          angle1+=TWO_PI;
        }
        if (angle1<arcEnd[j]&&angle1>arcBegin[j]) {
          indexes=append(indexes, j);
          angles=append(angles, angle1);
        }
      }
    }
    if (indexes.length==0) {
      float a;
      if(keyPressed&&keyCode==CONTROL){
        a=map(noise(count+i*5),0,1,arcBegin[i], arcEnd[i]);
      }else{
        a=random(arcBegin[i], arcEnd[i]);
      }
      float d=0;
      float tempX=lineX[i]+cos(a)*d;
      float tempY=lineY[i]+sin(a)*d;
      while (pow (tempX-width/2, 2)+pow(tempY-height/2, 2)<pow(sentenceRadius+20, 2)) {
        tempX=lineX[i]+cos(a)*d;
        tempY=lineY[i]+sin(a)*d;
        d+=1;
      }
      line(lineX[i]+cos(a)*lineRad[i]/2, lineY[i]+sin(a)*lineRad[i]/2, tempX, tempY);
    }
    else {
      int r;
      if(keyPressed&&keyCode==CONTROL){
        r=0;
      }else{
        r=floor(random(indexes.length));
      }
      int j=indexes[r];
      float a=angles[r]+PI;
      line(lineX[i]+cos(a)*lineRad[i]/2, lineY[i]+sin(a)*lineRad[i]/2, lineX[j]+cos(a+PI)*lineRad[j]/2, lineY[j]+sin(a+PI)*lineRad[j]/2);
      lineLengths=append(lineLengths, dist(lineX[i], lineY[i], lineX[j], lineY[j])+lineX[i]+lineY[i]+lineX[j]+lineY[j]);
      float[] templineX = {
      };
      float[] templineY = {
      };
      float[] temparcBegin = {
      };
      float[] temparcEnd = {
      };
      float[] templineRad = {
      };
      for (int k=0;k<lineX.length;k++) {
        if (k!=j&&k!=i) {
          templineX=append(templineX, lineX[k]);
          templineY=append(templineY, lineY[k]);
          temparcBegin=append(temparcBegin, arcBegin[k]);
          temparcEnd=append(temparcEnd, arcEnd[k]);
          templineRad=append(templineRad, lineRad[k]);
        }
      }
      lineX=templineX;
      lineY=templineY;
      arcBegin=temparcBegin;
      arcEnd=temparcEnd;
      lineRad=templineRad;
      i-=1;
    }
  }
}

public void makeDots(float mX, float mY, float r, int amnt, float pos, float scaleFactor) {
  noStroke();
  fill(fg);
  if (amnt==3) {
    ellipse(mX+cos(pos+PI)*r/1.4f, mY+sin(pos+PI)*r/1.4f, r/3*scaleFactor, r/3*scaleFactor);
  }
  ellipse(mX+cos(pos+PI+scaleFactor)*r/1.4f, mY+sin(pos+PI+scaleFactor)*r/1.4f, r/3*scaleFactor, r/3*scaleFactor);
  ellipse(mX+cos(pos+PI-scaleFactor)*r/1.4f, mY+sin(pos+PI-scaleFactor)*r/1.4f, r/3*scaleFactor, r/3*scaleFactor);
  noFill();
  stroke(fg);
}

float float1=0;
float float2=0;

public void makeArcs(float mX, float mY, float nX, float nY, float r1, float r2, float begin, float end) {
  float theta;
  float omega=0;
  float d = dist(mX, mY, nX, nY);
  theta=acos((pow(r1, 2)-pow(r2, 2)+pow(d, 2))/(2*d*r1));
  if (nX-mX<0) {
    omega=atan((mY-nY)/( mX-nX));
  }
  else if (nX-mX>0) {
    omega=PI+atan((mY-nY)/( mX-nX));
  }
  else if (nX-mX==0) {
    if (nY>mY) {
      omega=3*PI/2;
    }
    else {
      omega=PI/2;
    }
  }
  if (omega+theta-end>0) {
    arc(nX, nY, r1*2, r1*2, omega+theta, end+TWO_PI);
    arc(nX, nY, r1*2, r1*2, begin+TWO_PI, omega-theta);
  }
  else {
    arc(nX, nY, r1*2, r1*2, omega+theta, end);
    arc(nX, nY, r1*2, r1*2, begin+TWO_PI, omega-theta+TWO_PI);
  }
  if (omega+theta<end||omega-theta>begin) {
    strokeCap(SQUARE);
    stroke(bg);
    strokeWeight(4);
    // arc(nX, nY, r1*2, r1*2, omega-theta,omega+theta);
    strokeWeight(2);
    stroke(fg);
    strokeCap(ROUND);
  }
  theta=PI-acos((pow(r2, 2)-pow(r1, 2)+pow(d, 2))/(2*d*r2));
  if (nX-mX<0) {
    omega=atan((mY-nY)/( mX-nX));
  }
  else if (nX-mX>0) {
    omega=PI+atan((mY-nY)/( mX-nX));
  }
  else if (nX-mX==0) {
    if (nY>mY) {
      omega=3*PI/2;
    }
    else {
      omega=PI/2;
    }
  }
  arc(mX, mY, r2*2, r2*2, omega+theta, omega-theta+TWO_PI);
  float1=omega+theta;
  float2=omega-theta+TWO_PI;
}


}
