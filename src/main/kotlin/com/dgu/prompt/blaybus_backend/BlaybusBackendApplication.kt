package com.dgu.prompt.blaybus_backend
import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlaybusBackendApplication

fun main(args: Array<String>) {
	// .env 파일 로드
    val dotenv = dotenv {
        directory = "./" // 프로젝트 루트 디렉토리에서 로드
        ignoreIfMissing = true // .env 파일이 없으면 무시
    }

    // 환경 변수를 시스템 속성에 설정
    dotenv.entries().forEach { (key, value) ->
        System.setProperty(key, value)
    }

	runApplication<BlaybusBackendApplication>(*args)
}
