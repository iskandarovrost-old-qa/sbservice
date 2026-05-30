package com.iskandarov.sbservice.textparser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ArticleTextParser {
    String articlle = "";
    Map<String,String> personWithAction = new HashMap<String,String>();

    String person = "";
    String action = "";
    // for exact match
    String[] manNames = new String[]{
            "Алексей", "Андрей","Борис","Владимир","Венедикт","Геннадий", "Дмитрий", "Роман"
    };
    String[] wmnNames = new String[]{
            "Анна","Алина","Алевтина","Бронеслава","Софья"
    };
    String[] surnames = new String[]{
            "Александров","Абрамович", "Борисов","Валуа","Григорьев","Дывыдов","Жуковский","Зиновьев","Искандаров"
    };
    //Patters to find
    String[] manSurnamePatterns = new String[]{"[А-ЯЁ][а-яё]+(ов|ев|кий)"};
    String[] wmnSurnamePatterns = new String[]{"[А-ЯЁ][а-яё]+(ова|ева)"};
    String[] uniSurnamePatterns = new String[]{"[А-ЯЁ][а-яё]+(юк|янц|ай|аж|ум)"};

    //Actions
    String[] exactActions = new String[]{"пошел","поехал","съехал","поехал отдыхать","не платит налоги"};
    String[] actionsByPattern = new String[]{" [а-яё]+ала "," [а-яё]+ал "};


    public void parseTextPersonAction(String article){
        this.articlle = article;
        personWithAction.clear();

        Map<String,Integer> manNamesWithEntry = findTextEntries(article, manNames,0,article.length());
        if (!manNamesWithEntry.isEmpty()){
            findSurnameAndAction(manNamesWithEntry,
                    Stream.concat(Arrays.stream(manSurnamePatterns), Arrays.stream(uniSurnamePatterns)).toArray(String[]::new)); // Man might have universal surname
        }

        Map<String,Integer> wmnNamesWithEntry = findTextEntries(article, wmnNames,0,article.length());
        if (!wmnNamesWithEntry.isEmpty()){
            findSurnameAndAction(wmnNamesWithEntry,
                    Stream.concat(Arrays.stream(wmnSurnamePatterns), Arrays.stream(uniSurnamePatterns)).toArray(String[]::new)); // Man might have universal surname
        }
    }

    void findSurnameAndAction(Map<String,Integer> names, String[] surnames){
        String personFullName = "";
        String personAction = "";

        for (Map.Entry<String, Integer> entry : names.entrySet()) {
            personFullName = entry.getKey();
            personAction = "";

            Map<String, Integer> surnameWithEntry =
                    findTextEntries(articlle, surnames, entry.getValue() - 20, entry.getValue() +30);

            if (surnameWithEntry.size()==1){ //if found a single surname, use it to build the full name
                personFullName += (" " + surnameWithEntry.keySet().iterator().next());

            }

            Map<String, Integer> actionsWithEntry =
                    findTextEntries(articlle,
                            Stream.concat(Arrays.stream(exactActions), Arrays.stream(actionsByPattern)).toArray(String[]::new),
                            entry.getValue() - 20,
                            entry.getValue() +50);

            if (!actionsWithEntry.isEmpty()){
                for (String action : actionsWithEntry.keySet()) {
                    personAction += (", " + action);
                }
            }

            personWithAction.put(personFullName, personAction);
        }


    }


    boolean parseNames(String text, String[] patterns){
        Map<String, Integer> nameEntries = findTextEntries(text,patterns,0,text.length());
        if (!nameEntries.isEmpty()){
            return true;
        }
        return false;
    }

    Map<String, Integer> findTextEntries(String text, String[] patterns, int begin, int end)
    {
        if (begin < 0)  begin = 0;
        if (end > text.length()) end = text.length();

        try {
            text = text.substring(begin, end); //find in surroundings
        } catch (IndexOutOfBoundsException e) {
            System.out.println("incorrect substr set for findTextEntries"); //todo:log
        }

        int entryPoint = 0;
        boolean found = false;
        Pattern namePattern;        //TODO: rename variable
        Map<String, Integer> textEntry = new HashMap<String, Integer>();

        // find pattern entry
        for (String pattern : patterns) {
            namePattern = Pattern.compile(pattern);
            Matcher matcher = namePattern.matcher(text);
            while (matcher.find()){
                found = true;
                entryPoint = matcher.start();
                textEntry.put(matcher.group(), entryPoint);
            }
        }

        return textEntry;
    }

    public String getPersonWithActionString(){
        StringBuilder parserResult = new StringBuilder();
        for (Map.Entry<String, String> entry : personWithAction.entrySet()) {
            parserResult.append(entry.getKey()).append(" => ").append(entry.getValue()).append("; ");
        }
        return parserResult.toString();
    }

    public List<PersonWithAction> getPersonWithActionList(){
        List<PersonWithAction> personsWithAction= new ArrayList<PersonWithAction>();
        for (Map.Entry<String, String> entry : personWithAction.entrySet()) {
            personsWithAction.add(new PersonWithAction(entry.getKey(), entry.getValue()));
            //parserResult.append(entry.getKey()).append(" => ").append(entry.getValue()).append("; ");
        }
        return personsWithAction;
    }
}
