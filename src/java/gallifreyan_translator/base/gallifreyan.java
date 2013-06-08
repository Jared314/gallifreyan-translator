// Gallifreyan Translator v0.3.0
// Loren Sherman
// http://blackhatguy.deviantart.com/art/Gallifreyan-Translator-280910144

package gallifreyan_translator.base;

import processing.core.PApplet;
import processing.core.PGraphics;

public class gallifreyan {

  // This function exists because there is both a keyPressed field and method in
  // the PApplet object and clojure, when calling it, chooses the method over the field,
  // rendering the field inaccessible from clojure.
  public static boolean getKeyPressed(PApplet applet){ return applet.keyPressed; }

  public static class ArcPoint {
    public final float float1;
    public final float float2;
    public ArcPoint(float f1, float f2) {
      float1 = f1;
      float2 = f2;
    }
  }



  public static String transliterate(String english){
    //TODO: Replace. Adapted from existing code.
    return english
      .trim()
      .toLowerCase()
      .replace(" -", "-")
      .replace("- ", "-")
      .replace("-", "- ")
      .replace("ch", "#")
      .replace("sh", "$")
      .replace("th", "%")
      .replace("ng", "&")
      .replace("qu", "q");
  }

  private static float sin(float angle){ return PApplet.sin(angle); }
  private static float cos(float angle){ return PApplet.cos(angle); }
  private static float atan(float value){ return PApplet.atan(value); }
  private static float acos(float value){ return PApplet.acos(value); }
  private static float pow(float n, float e){ return PApplet.pow(n, e); }
  private static int round(float n){ return PApplet.round(n); }
  private static float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return PApplet.dist(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  private static float dist(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    return PApplet.dist(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
  }

  private static float PI = PApplet.PI;
  private static float TWO_PI = PApplet.TWO_PI;

  private static int sentenceCount(String text){
    int spaces = 0;
    int sentences = 1;

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      boolean isNotEnd = i < text.length() - 1;

      if (c == ' ') spaces++;

      if (isNotEnd
          && (c == '.' || c == '!' ||c == '?')
          && text.charAt(i + 1) == ' ')
      {
        sentences++;
      }
    }

    return (spaces == 0) ? 0 : sentences;
  }



  public static PGraphics draw(PApplet applet, String english, int fg, int bg, float sentenceRadius, float count){

    if(english.length() < 1) return null;

    int type = sentenceCount(english);
    if(type > 1) return null; // Does not handle multiple sentences

    PGraphics result = applet.createGraphics(1024, 600, PApplet.P2D);
    result.beginDraw();
    result.background(bg);
    result.smooth();
    result.stroke(fg);
    result.strokeWeight(1);

    float[] wordRadius = {};
    ArcPoint ap = new ArcPoint(0,0);

    float charCount=0;
    if(english.length() > 1 && english.charAt(english.length()-1)==' '){
      String oldenglish=english;
      english="";
      for(int n=0;n<oldenglish.length()-1;n++){
        english=english+oldenglish.charAt(n);
      }
    }
    String Word = "";
    String[] Sentence = {};

    Sentence = applet.split(english, " ");
    String[][] sentence = new String[Sentence.length][];
    char[] punctuation = new char[Sentence.length];
    boolean[][] apostrophes = new boolean[Sentence.length][100];
    for (int j=0;j<Sentence.length;j++) {
      String[] word= {
      };
      Sentence[j]=applet.join(applet.split(Sentence[j], " "), "");
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
            word=applet.append(word, applet.str(Sentence[j].charAt(i)));
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
          word=applet.append(word, applet.str(Sentence[j].charAt(i)));
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
    result.stroke(fg);
    if (type>0) {
      result.strokeWeight(3);
      result.ellipse(applet.width/2, applet.height/2, sentenceRadius*2, sentenceRadius*2);
    }
    result.strokeWeight(4);
    result.ellipse(applet.width/2, applet.height/2, sentenceRadius*2+40, sentenceRadius*2+40);
    float pos=PI/2;
    float maxRadius=0;
    for (int i=0;i<sentence.length;i++) {
      wordRadius=applet.append(wordRadius, applet.constrain(sentenceRadius*sentence[i].length/charCount*1.2f, 0, sentenceRadius/2));
      if (wordRadius[i]>maxRadius) {
        maxRadius=wordRadius[i];
      }
    }
    float scaleFactor = sentenceRadius/(maxRadius+(sentenceRadius/2));
    float distance=scaleFactor*sentenceRadius/2;
    for (int i=0;i<wordRadius.length;i++) {
      wordRadius[i]*=scaleFactor;
    }
    float[] x= {};
    float[] y= {};

    result.stroke(fg);

    for (int i=0;i<sentence.length;i++) {
      x=applet.append(x, applet.width/2+distance*cos(pos));
      y=applet.append(y, applet.height/2+distance*sin(pos));
      int nextIndex=0;
      if (i!=sentence.length-1) {
        nextIndex=i+1;
      }
      pos-=PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*TWO_PI;
      float pX = applet.width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius;
      float pY = applet.height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius;
      switch(punctuation[i]){
        case '.':
        result.ellipse(pX,pY,20,20);
        break;
        case '?':
        makeDots(result, applet.width/2,applet.height/2,sentenceRadius*1.4f,2,-1.2f,0.1f, fg);
        break;
        case '!':
        makeDots(result, applet.width/2,applet.height/2,sentenceRadius*1.4f,3,-1.2f,0.1f, fg);
        break;
        case '"':
        result.line(applet.width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,applet.height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,applet.width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20),applet.height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20));
        break;
        case '-':
        result.line(applet.width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,applet.height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius,applet.width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20),applet.height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*(sentenceRadius+20));
        result.line(applet.width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length+0.3f)/(2*charCount)*PI)*sentenceRadius,applet.height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length+0.2f)/(2*charCount)*PI)*sentenceRadius,applet.width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length+0.2f)/(2*charCount)*PI)*(sentenceRadius+20),applet.height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length+0.3f)/(2*charCount)*PI)*(sentenceRadius+20));
        result.line(applet.width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length-0.3f)/(2*charCount)*PI)*sentenceRadius,applet.height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length-0.2f)/(2*charCount)*PI)*sentenceRadius,applet.width/2+cos(pos+(sentence[i].length+sentence[nextIndex].length-0.2f)/(2*charCount)*PI)*(sentenceRadius+20),applet.height/2+sin(pos+(sentence[i].length+sentence[nextIndex].length-0.3f)/(2*charCount)*PI)*(sentenceRadius+20));
        break;
        case ',':
        result.fill(fg);
        result.ellipse(pX,pY,20,20);
        result.noFill();
        break;
        case ';':
        result.fill(fg);
        result.ellipse(applet.width/2+cos(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius-10,applet.height/2+sin(pos+PApplet.parseFloat(sentence[i].length+sentence[nextIndex].length)/(2*charCount)*PI)*sentenceRadius-10,10,10);
        result.noFill();
        break;
        case ':':
        result.ellipse(pX,pY,25,25);
        result.strokeWeight(2);
        result.ellipse(pX,pY,15,15);
        result.strokeWeight(4);
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
      int index = round(applet.map(angle1, 0, TWO_PI, 0, PApplet.parseFloat(sentence[i].length)));
      if (index==sentence[i].length) {
        index=0;
      }
      char tempChar=sentence[i][index].charAt(0);
      if ((tempChar=='t'||tempChar=='$'||tempChar=='r'||tempChar=='s'||tempChar=='v'||tempChar=='w')&&type>0) {
        nested[i][index]=true;
        wordRadius[i]=applet.constrain(wordRadius[i]*1.2f, 0, maxRadius*scaleFactor);
        while (dist(x[i], y[i], x[otherIndex], y[otherIndex])>wordRadius[i]+wordRadius[otherIndex]) {
          x[i]=applet.lerp(x[i], x[otherIndex], 0.05f);
          y[i]=applet.lerp(y[i], y[otherIndex], 0.05f);
        }
      }
    }
    float[] lineX = {};
    float[] lineY = {};
    float[] arcBegin = {};
    float[] arcEnd = {};
    float[] lineRad = {};
    result.strokeWeight(2);
    if (type==0) {
      wordRadius[0]=sentenceRadius*0.9f;
      x[0]=applet.width/2;
      y[0]=applet.height/2;
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
          while (pow(tempX-applet.width/2, 2)+pow(tempY-applet.height/2, 2)<pow(sentenceRadius+20, 2)) {
            tempX=x[i]+cos(a)*d;
            tempY=y[i]+sin(a)*d;
            d+=1;
          }
          result.line(x[i]+cos(a)*wordRadius[i], y[i]+sin(a)*wordRadius[i], tempX, tempY);
          a=pos+PI/sentence[i].length+0.1f;
          d=0;
          tempX=x[i];
          tempY=y[i];
          while (pow(tempX-applet.width/2, 2)+pow(tempY-applet.height/2, 2)<pow(sentenceRadius+20, 2)) {
            tempX=x[i]+cos(a)*d;
            tempY=y[i]+sin(a)*d;
            d+=1;
          }
          result.line(x[i]+cos(a)*wordRadius[i], y[i]+sin(a)*wordRadius[i], tempX, tempY);
        }
        boolean vowel=true;
        float tempX=0;
        float tempY=0;

        //single vowels

        if (sentence[i][j].charAt(0)=='a') {
          tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
          tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
          result.ellipse(tempX, tempY, letterRadius, letterRadius);
        }
        else if (sentence[i][j].charAt(0)=='e') {
          tempX=x[i]+cos(pos)*(wordRadius[i]);
          tempY=y[i]+sin(pos)*(wordRadius[i]);
          result.ellipse(tempX, tempY, letterRadius, letterRadius);
        }
        else if (sentence[i][j].charAt(0)=='i') {
          tempX=x[i]+cos(pos)*(wordRadius[i]);
          tempY=y[i]+sin(pos)*(wordRadius[i]);
          result.ellipse(tempX, tempY, letterRadius, letterRadius);
          lineX=applet.append(lineX, tempX);
          lineY=applet.append(lineY, tempY);
          arcBegin=applet.append(arcBegin, pos+PI/2);
          arcEnd=applet.append(arcEnd, pos+3*PI/2);
          lineRad=applet.append(lineRad, letterRadius);
        }
        else if (sentence[i][j].charAt(0)=='o') {
          tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius/1.6f);
          tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius/1.6f);
          result.ellipse(tempX, tempY, letterRadius, letterRadius);
        }
        else if (sentence[i][j].charAt(0)=='u') {
          tempX=x[i]+cos(pos)*(wordRadius[i]);
          tempY=y[i]+sin(pos)*(wordRadius[i]);
          result.ellipse(tempX, tempY, letterRadius, letterRadius);
          lineX=applet.append(lineX, tempX);
          lineY=applet.append(lineY, tempY);
          arcBegin=applet.append(arcBegin, pos-PI/2);
          arcEnd=applet.append(arcEnd, pos+PI/2);
          lineRad=applet.append(lineRad, letterRadius);
        }
        else {
          vowel=false;
        }

        if (vowel) {
          result.arc(x[i], y[i], wordRadius[i]*2, wordRadius[i]*2, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
          if (sentence[i][j].length()==1) {
          }
          else {

            //double vowels

            if (sentence[i][j].charAt(1)=='@') {
              result.ellipse(tempX, tempY, letterRadius*1.3f, letterRadius*1.3f);
            }
          }
        }
        else {

          // consonants

          if (sentence[i][j].charAt(0)=='b'||sentence[i][j].charAt(0)=='#'||sentence[i][j].charAt(0)=='d'||sentence[i][j].charAt(0)=='f'||sentence[i][j].charAt(0)=='g'||sentence[i][j].charAt(0)=='h') {
            tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius*0.95f);
            tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius*0.95f);
            ap = makeArcs(result, tempX, tempY, x[i], y[i], wordRadius[i], letterRadius, pos-PI/sentence[i].length, pos+PI/sentence[i].length, fg, bg);
            int lines=0;
            if (sentence[i][j].charAt(0)=='#') {
              makeDots(result, tempX, tempY, letterRadius, 2, pos, 1, fg);
            }
            else if (sentence[i][j].charAt(0)=='d') {
              makeDots(result, tempX, tempY, letterRadius, 3, pos, 1, fg);
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
              lineX=applet.append(lineX, tempX);
              lineY=applet.append(lineY, tempY);
              arcBegin=applet.append(arcBegin, pos+0.5f);
              arcEnd=applet.append(arcEnd, pos+TWO_PI-0.5f);
              lineRad=applet.append(lineRad, letterRadius*2);
            }
            if (sentence[i][j].length()>1) {
              int vowelIndex=1;
              if (sentence[i][j].charAt(1)=='@') {
                ap = makeArcs(result, tempX, tempY, x[i], y[i], wordRadius[i], letterRadius*1.3f, pos+TWO_PI, pos-TWO_PI, fg, bg);
                vowelIndex=2;
              }
              if (sentence[i][j].length()==vowelIndex) {
                pos-=TWO_PI/sentence[i].length;
                continue;
              }
              if (sentence[i][j].charAt(vowelIndex)=='a') {
                tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
                tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='e') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='i') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
                lineX=applet.append(lineX, tempX);
                lineY=applet.append(lineY, tempY);
                arcBegin=applet.append(arcBegin, pos+PI/2);
                arcEnd=applet.append(arcEnd, pos+3*PI/2);
                lineRad=applet.append(lineRad, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='o') {
                tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius*2);
                tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius*2);
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='u') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
                lineX=applet.append(lineX, tempX);
                lineY=applet.append(lineY, tempY);
                arcBegin=applet.append(arcBegin, pos-PI/2);
                arcEnd=applet.append(arcEnd, pos+PI/2);
                lineRad=applet.append(lineRad, letterRadius);
              }
              if (sentence[i][j].length()==(vowelIndex+2)) {
                if (sentence[i][j].charAt(vowelIndex+1)=='@') {
                  result.ellipse(tempX, tempY, letterRadius*1.3f, letterRadius*1.3f);
                }
              }
            }
          }
          if (sentence[i][j].charAt(0)=='j'||sentence[i][j].charAt(0)=='k'||sentence[i][j].charAt(0)=='l'||sentence[i][j].charAt(0)=='m'||sentence[i][j].charAt(0)=='n'||sentence[i][j].charAt(0)=='p') {
            tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius);
            tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius);
            result.ellipse(tempX, tempY, letterRadius*1.9f, letterRadius*1.9f);
            result.arc(x[i], y[i], wordRadius[i]*2, wordRadius[i]*2, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
            int lines=0;
            if (sentence[i][j].charAt(0)=='k') {
              makeDots(result, tempX, tempY, letterRadius, 2, pos, 1, fg);
            }
            else if (sentence[i][j].charAt(0)=='l') {
              makeDots(result, tempX, tempY, letterRadius, 3, pos, 1, fg);
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
              lineX=applet.append(lineX, tempX);
              lineY=applet.append(lineY, tempY);
              arcBegin=applet.append(arcBegin, 0);
              arcEnd=applet.append(arcEnd, TWO_PI);
              lineRad=applet.append(lineRad, letterRadius*1.9f);
            }
            if (sentence[i][j].length()>1) {
              int vowelIndex=1;
              if (sentence[i][j].charAt(1)=='@') {
                result.ellipse(tempX, tempY, letterRadius*2.3f, letterRadius*2.3f);
                vowelIndex=2;
              }
              if (sentence[i][j].length()==vowelIndex) {
                pos-=TWO_PI/sentence[i].length;
                continue;
              }
              if (sentence[i][j].charAt(vowelIndex)=='a') {
                tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
                tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='e') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='i') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
                lineX=applet.append(lineX, tempX);
                lineY=applet.append(lineY, tempY);
                arcBegin=applet.append(arcBegin, pos+PI/2);
                arcEnd=applet.append(arcEnd, pos+3*PI/2);
                lineRad=applet.append(lineRad, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='o') {
                tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius*2);
                tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius*2);
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='u') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
                lineX=applet.append(lineX, tempX);
                lineY=applet.append(lineY, tempY);
                arcBegin=applet.append(arcBegin, pos-PI/2);
                arcEnd=applet.append(arcEnd, pos+PI/2);
                lineRad=applet.append(lineRad, letterRadius);
              }
              if (sentence[i][j].length()==(vowelIndex+2)) {
                if (sentence[i][j].charAt(vowelIndex+1)=='@') {
                  result.ellipse(tempX, tempY, letterRadius*1.3f, letterRadius*1.3f);
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
              ap = makeArcs(result, x[nextIndex], y[nextIndex], x[i], y[i], wordRadius[i], wordRadius[nextIndex]+20, pos-PI/sentence[i].length, pos+PI/sentence[i].length, fg, bg);
            }
            else {
              ap = makeArcs(result, tempX, tempY, x[i], y[i], wordRadius[i], letterRadius*1.5f, pos-PI/sentence[i].length, pos+PI/sentence[i].length, fg, bg);
            }
            int lines=0;
            if (sentence[i][j].charAt(0)=='$') {
              if (nested[i][j]) {
                makeDots(result, x[nextIndex], y[nextIndex], (wordRadius[nextIndex]*1.4f)+14, 2, angle1, wordRadius[nextIndex]/500, fg);
              }
              else {
                makeDots(result, tempX, tempY, letterRadius*2.6f, 2, pos, 0.5f, fg);
              }
            }
            else if (sentence[i][j].charAt(0)=='r') {
              if (nested[i][j]) {
                makeDots(result, x[nextIndex], y[nextIndex], (wordRadius[nextIndex]*1.4f)+14, 3, angle1, wordRadius[nextIndex]/500, fg);
              }
              else {
                makeDots(result, tempX, tempY, letterRadius*2.6f, 3, pos, 0.5f, fg);
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
                lineX=applet.append(lineX, x[nextIndex]);
                lineY=applet.append(lineY, y[nextIndex]);
                arcBegin=applet.append(arcBegin, ap.float1);
                arcEnd=applet.append(arcEnd, ap.float2);
                lineRad=applet.append(lineRad, wordRadius[nextIndex]*2+40);
              }
            }
            else {
              for (int k=0;k<lines;k++) {
                lineX=applet.append(lineX, tempX);
                lineY=applet.append(lineY, tempY);
                arcBegin=applet.append(arcBegin, ap.float1);
                arcEnd=applet.append(arcEnd, ap.float2);
                lineRad=applet.append(lineRad, letterRadius*3);
              }
            }
            if (sentence[i][j].length()>1) {
              if (sentence[i][j].charAt(1)=='@') {
                if (nested[i][j]) {
                  ap = makeArcs(result, x[nextIndex], y[nextIndex], x[i], y[i], wordRadius[i], (wordRadius[nextIndex]+20)*1.2f, pos+TWO_PI, pos-TWO_PI, fg, bg);
                }
                else {
                  ap = makeArcs(result, tempX, tempY, x[i], y[i], wordRadius[i], letterRadius*1.8f, pos+TWO_PI, pos-TWO_PI, fg, bg);
                }
              }
            }
          }
          if (sentence[i][j].charAt(0)=='%'||sentence[i][j].charAt(0)=='y'||sentence[i][j].charAt(0)=='z'||sentence[i][j].charAt(0)=='&'||sentence[i][j].charAt(0)=='q'||sentence[i][j].charAt(0)=='x') {
            tempX=x[i]+cos(pos)*(wordRadius[i]);
            tempY=y[i]+sin(pos)*(wordRadius[i]);
            result.ellipse(tempX, tempY, letterRadius*2, letterRadius*2);
            result.arc(x[i], y[i], wordRadius[i]*2, wordRadius[i]*2, pos-PI/sentence[i].length, pos+PI/sentence[i].length);
            int lines=0;
            if (sentence[i][j].charAt(0)=='y') {
              makeDots(result, tempX, tempY, letterRadius, 2, pos, 1, fg);
            }
            else if (sentence[i][j].charAt(0)=='z') {
              makeDots(result, tempX, tempY, letterRadius, 3, pos, 1, fg);
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
              lineX=applet.append(lineX, tempX);
              lineY=applet.append(lineY, tempY);
              arcBegin=applet.append(arcBegin, 0);
              arcEnd=applet.append(arcEnd, TWO_PI);
              lineRad=applet.append(lineRad, letterRadius*2);
            }
            if (sentence[i][j].length()>1) {
              int vowelIndex=1;
              if (sentence[i][j].charAt(1)=='@') {
                result.ellipse(tempX, tempY, letterRadius*2.3f, letterRadius*2.3f);
                vowelIndex=2;
              }
              if (sentence[i][j].length()==vowelIndex) {
                pos-=TWO_PI/sentence[i].length;
                continue;
              }
              if (sentence[i][j].charAt(vowelIndex)=='a') {
                tempX=x[i]+cos(pos)*(wordRadius[i]+letterRadius/2);
                tempY=y[i]+sin(pos)*(wordRadius[i]+letterRadius/2);
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='e') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='i') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
                lineX=applet.append(lineX, tempX);
                lineY=applet.append(lineY, tempY);
                arcBegin=applet.append(arcBegin, pos+PI/2);
                arcEnd=applet.append(arcEnd, pos+3*PI/2);
                lineRad=applet.append(lineRad, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='o') {
                tempX=x[i]+cos(pos)*(wordRadius[i]-letterRadius);
                tempY=y[i]+sin(pos)*(wordRadius[i]-letterRadius);
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
              }
              else if (sentence[i][j].charAt(vowelIndex)=='u') {
                result.ellipse(tempX, tempY, letterRadius, letterRadius);
                lineX=applet.append(lineX, tempX);
                lineY=applet.append(lineY, tempY);
                arcBegin=applet.append(arcBegin, pos-PI/2);
                arcEnd=applet.append(arcEnd, pos+PI/2);
                lineRad=applet.append(lineRad, letterRadius);
              }
              if (sentence[i][j].length()==(vowelIndex+2)) {
                if (sentence[i][j].charAt(vowelIndex+1)=='@') {
                  result.ellipse(tempX, tempY, letterRadius*1.8f, letterRadius*1.8f);
                }
              }
            }
          }
        }
        pos-=TWO_PI/sentence[i].length;
      }
    }


    applet.strokeWeight(2);
    float[] lineLengths= {};
    applet.stroke(fg);

    for (int i=0;i<lineX.length;i++) {
      int[] indexes = {};
      float[]angles = {};

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
            indexes=applet.append(indexes, j);
            angles=applet.append(angles, angle1);
          }
        }
      }
      if (indexes.length==0) {
        float a;
//        if(applet.keyPressed&&applet.keyCode==PApplet.CONTROL){
          a=applet.map(applet.noise(count+i*5),0,1,arcBegin[i], arcEnd[i]);
//        }else{
//          a=applet.random(arcBegin[i], arcEnd[i]);
//        }
        float d=0;
        float tempX=lineX[i]+cos(a)*d;
        float tempY=lineY[i]+sin(a)*d;
        while (pow(tempX-applet.width/2, 2)+pow(tempY-applet.height/2, 2)<pow(sentenceRadius+20, 2)) {
          tempX=lineX[i]+cos(a)*d;
          tempY=lineY[i]+sin(a)*d;
          d+=1;
        }
        result.line(lineX[i]+cos(a)*lineRad[i]/2, lineY[i]+sin(a)*lineRad[i]/2, tempX, tempY);
      }
      else {
        int r;
//        if(applet.keyPressed&&applet.keyCode==PApplet.CONTROL){
          r=0;
//        }else{
//          r=applet.floor(applet.random(indexes.length));
//        }
        int j=indexes[r];
        float a=angles[r]+PI;
        result.line(lineX[i]+cos(a)*lineRad[i]/2, lineY[i]+sin(a)*lineRad[i]/2, lineX[j]+cos(a+PI)*lineRad[j]/2, lineY[j]+sin(a+PI)*lineRad[j]/2);
        lineLengths=applet.append(lineLengths, dist(lineX[i], lineY[i], lineX[j], lineY[j])+lineX[i]+lineY[i]+lineX[j]+lineY[j]);
        float[] templineX = {};
        float[] templineY = {};
        float[] temparcBegin = {};
        float[] temparcEnd = {};
        float[] templineRad = {};
        for (int k=0;k<lineX.length;k++) {
          if (k!=j&&k!=i) {
            templineX=applet.append(templineX, lineX[k]);
            templineY=applet.append(templineY, lineY[k]);
            temparcBegin=applet.append(temparcBegin, arcBegin[k]);
            temparcEnd=applet.append(temparcEnd, arcEnd[k]);
            templineRad=applet.append(templineRad, lineRad[k]);
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

    result.endDraw();
    return result;//.get(0, 0, result.width, result.height);
  }

  public static void makeDots(PGraphics g, float mX, float mY, float r, int amnt, float pos, float scaleFactor, int fg) {
    g.noStroke();
    g.fill(fg);
    if (amnt==3) {
      g.ellipse(mX+cos(pos+PI)*r/1.4f, mY+sin(pos+PI)*r/1.4f, r/3*scaleFactor, r/3*scaleFactor);
    }
    g.ellipse(mX+cos(pos+PI+scaleFactor)*r/1.4f, mY+sin(pos+PI+scaleFactor)*r/1.4f, r/3*scaleFactor, r/3*scaleFactor);
    g.ellipse(mX+cos(pos+PI-scaleFactor)*r/1.4f, mY+sin(pos+PI-scaleFactor)*r/1.4f, r/3*scaleFactor, r/3*scaleFactor);
    g.noFill();
    g.stroke(fg);
  }


  public static ArcPoint makeArcs(PGraphics g, float mX, float mY, float nX, float nY, float r1, float r2, float begin, float end, int fg, int bg) {
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
      g.arc(nX, nY, r1*2, r1*2, omega+theta, end+TWO_PI);
      g.arc(nX, nY, r1*2, r1*2, begin+TWO_PI, omega-theta);
    }
    else {
      g.arc(nX, nY, r1*2, r1*2, omega+theta, end);
      g.arc(nX, nY, r1*2, r1*2, begin+TWO_PI, omega-theta+TWO_PI);
    }
    if (omega+theta<end||omega-theta>begin) {
      g.strokeCap(g.SQUARE);
      g.stroke(bg);
      g.strokeWeight(4);
      // g.arc(nX, nY, r1*2, r1*2, omega-theta,omega+theta);
      g.strokeWeight(2);
      g.stroke(fg);
      g.strokeCap(g.ROUND);
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
    g.arc(mX, mY, r2*2, r2*2, omega+theta, omega-theta+TWO_PI);

    return new ArcPoint(omega+theta, omega-theta+TWO_PI);
  }
}