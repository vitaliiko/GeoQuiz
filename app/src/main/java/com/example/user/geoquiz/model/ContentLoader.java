package com.example.user.geoquiz.model;

import android.content.Context;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentLoader {

   private static String loadFromFile(String fileName) {
       File sdcard = Environment.getExternalStorageDirectory();
       File file = new File(sdcard, fileName);
       StringBuilder text = new StringBuilder();
       try {
           BufferedReader br = new BufferedReader(new FileReader(file));
           String line;
           while ((line = br.readLine()) != null) {
               text.append(line);
               text.append('\n');
           }
           br.close();
       }
       catch (IOException e) {
           e.printStackTrace();
       }
       return text.toString();
   }

    public static List<Question> loadContent(Context context, String quizFileName) {
        List<Question> questions = new ArrayList<>();
        String imageName = null;
        String questionText = null;
        List<String> answers = new ArrayList<>();
        Integer rightAnswer = null;
        int i = 0;
        for (String s : loadFromFile(quizFileName).split("\r\n")) {
            if (!s.equals("***")) {
                if (imageName == null) {
                    imageName = s;
                } else if (questionText == null) {
                    questionText = s;
                } else {
                    String answer = s;
                    if (s.startsWith("*")) {
                        answer = s.replace("*", "");
                        rightAnswer = i;
                    }
                    answers.add(answer);
                    i++;
                }
            } else {
                int imageId = context
                        .getResources()
                        .getIdentifier(imageName, "drawable", context.getPackageName());

                questions.add(new Question(imageId, questionText, answers, rightAnswer));
                imageName = null;
                questionText = null;
                answers.clear();
                i = 0;
            }
        }
        return questions;
    }
}
