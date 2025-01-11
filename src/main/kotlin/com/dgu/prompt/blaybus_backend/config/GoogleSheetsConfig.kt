package com.dgu.prompt.blaybus_backend.config

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class GoogleSheetsConfig {

    private val APPLICATION_NAME = "Google Sheets API Integration"
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()
    private val CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json"

    @Bean
    fun sheetsService(): Sheets {
        // HTTP Transport 설정
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        // credentials.json 파일에서 인증 정보 로드
        val credentials = FileInputStream(CREDENTIALS_FILE_PATH).use { inputStream ->
            GoogleCredentials.fromStream(inputStream)
                .createScoped(listOf("https://www.googleapis.com/auth/spreadsheets.readonly"))
        }

        // Sheets API 서비스 객체 생성
        return Sheets.Builder(
            httpTransport,
            JSON_FACTORY,
            HttpCredentialsAdapter(credentials)
        )
            .setApplicationName(APPLICATION_NAME)
            .build()
    }
}
