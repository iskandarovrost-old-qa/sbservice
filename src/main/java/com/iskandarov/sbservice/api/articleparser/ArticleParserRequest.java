package com.iskandarov.sbservice.api.articleparser;

public class ArticleParserRequest {
    String articleBody;
    public ArticleParserRequest(String articleBody){
        this.articleBody = articleBody;
    }
    public ArticleParserRequest(){
        articleBody ="";
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public String getArticleBody() {
        return articleBody;
    }
}
