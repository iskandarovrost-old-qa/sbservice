package com.iskandarov.sbservice.api.articleparser;

import com.iskandarov.sbservice.textparser.ArticleTextParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ParseArticle")
public class ArticleParserController {

    static final ArticleTextParser parser = new ArticleTextParser();

    // POST endpoint: http://localhost:8080/api/ParseArticle
    @PostMapping
    public ArticleParserResponse parseArticleText(@RequestBody ArticleParserRequest request) {
        String articleText = request.articleBody;
        parser.parseTextPersonAction(articleText);

        ArticleParserResponse response = new ArticleParserResponse(request.articleBody, parser.getPersonWithActionString(), parser.getPersonWithActionList());


        return response;
    }
}
