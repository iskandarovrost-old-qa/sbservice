package com.iskandarov.sbservice.api.articleparser;

import com.iskandarov.sbservice.textparser.PersonWithAction;



import java.util.List;

public class ArticleParserResponse {
    String articleBody;
    String articleParseResult;
    List<PersonWithAction> personWithActionList;

    public ArticleParserResponse(String articleBody, String personWithActionString, List<PersonWithAction> personWithActionList) {
        this.articleBody = articleBody;
        this.articleParseResult = personWithActionString;
        this.personWithActionList = personWithActionList;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public List<PersonWithAction> getPersonWithActionList() {
        return personWithActionList;
    }

    public String getArticleParseResult() {
        return articleParseResult;
    }
}
