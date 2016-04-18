package com.example.user.geoquiz.utils;

import android.content.Context;

import com.example.user.geoquiz.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ContentLoader {

   private static String loadFromFile(Context context, int fileId) {
       StringBuilder text = new StringBuilder();
       InputStream is = context.getResources().openRawResource(fileId);
       BufferedReader br = new BufferedReader(new InputStreamReader(is));
       String readLine;
       try {
           while ((readLine = br.readLine()) != null) {
               text.append(readLine).append("\n");
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
       return text.toString();
   }

    public static List<Question> loadContent(Context context, int quizFileId) {
        List<Question> questions = new ArrayList<>();
        String questionText = "Where this building is located?";
        String imageName = null;
        String imageDescription = null;
        List<String> answers = new ArrayList<>();
        Integer rightAnswer = null;
        int i = 0;
        String resource = loadFromFile(context, quizFileId);
        for (String s : resource.split("\n")) {
            if (!s.equals("***")) {
                if (imageName == null) {
                    imageName = s;
                } else if (imageDescription == null) {
                    imageDescription = s;
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

                Question question = new Question(imageId, imageDescription, questionText, rightAnswer);
                question.copyAnswers(answers);
                questions.add(question);
                imageName = null;
                imageDescription = null;
                answers.clear();
                i = 0;
            }
        }
        return questions;
    }
}
