package com.example.api.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

/**
 * Api router functions
 * @author tree
 */
@Configuration
class ApiRouterFunction(
  private val apiRouterHandler: ApiRouterHandler,
  private val sampleApiRouterHandler: SampleApiRouterHandler
) {

  /**
   * Routing
   * @return server response
   */
  @Bean
  fun routes(): RouterFunction<ServerResponse> = router {
    (accept(MediaType.APPLICATION_JSON_UTF8) and "/api").nest {
      /**
       * @api {get} /api/prefecture/ all prefectures
       * @apiName all refecture
       * @apiDescription Get all prefectures from database
       * @apiGroup Prefecture
       *
       * @apiSuccess {String} prefectureCd 都道府県コード
       *
       * prefecture.prefecture_cd
       *
       * @apiSuccess {String} prefectureName 都道府県名
       *
       * prefecture.prefecture_name
       *
       * @apiSuccessExample {json} Success-Response:
       * [
       * 	{
       * 		"prefectureCd" : "01",
       * 		"prefectureName" : "北海道"
       * 	},
       * 	{
       * 		"prefectureCd" : "02",
       * 		"prefectureName" : "青森県"
       * 	},
       * 	{
       * 		"prefectureCd" : "03",
       * 		"prefectureName" : "岩手県"
       * 	}
       * ]
       */
      GET("/prefecture/", apiRouterHandler::handlePrefectures)

      /**
       * @api {get} /api/prefecture/:prefectureCd specify prefecture
       * @apiName specify prefecture
       * @apiDescription Get specify prefecture from database
       * @apiGroup Prefecture
       *
       * @apiParam {String{..2}} prefectureCd 都道府県コード
       *
       * @apiSuccess {String} prefectureCd 都道府県コード
       *
       * prefecture.prefecture_cd
       *
       * @apiSuccess {String} prefectureName 都道府県名
       *
       * prefecture.prefecture_name
       *
       * @apiSuccessExample {json} Success-Response:
       * [
       * 	{
       * 		"prefectureCd" : "01",
       * 		"prefectureName" : "北海道"
       * 	}
       * ]
       */
      GET("/prefecture/{prefectureCd:[0-4][0-9]}", apiRouterHandler::handlePrefecture)

      /**
       * @api {get} /api/bcrypt/:rawPassword generate bcrypt password
       * @apiName generate bcrypt password
       * @apiDescription Generate bcrypt password
       * @apiGroup Security
       *
       * @apiSuccessExample {json} Success-Response:
       * $2a$10$7dEmTTKlLT4hIyj7TcMHreUgeoA0Lsa8z9.KTN.Nz0etEojORsXQm
       */
      GET("/bcrypt/{rawPassword}", apiRouterHandler::handleBcrypt)
    }
  }

  /**
   * Sample routing
   * @return server response
   */
  @Bean
  fun sampleRoutes(): RouterFunction<ServerResponse> = router {
    (accept(MediaType.APPLICATION_JSON_UTF8) and "/sample").nest {
      GET("/send-text-mail/", sampleApiRouterHandler::handleSendTextMail)
      GET("/send-html-mail/", sampleApiRouterHandler::handleSendHtmlMail)
      GET("/send-multipart-mail/", sampleApiRouterHandler::handleSendMultipartMail)
      POST("/recieve-json-request/", sampleApiRouterHandler::handleJsonRequest)
    }
  }
}
