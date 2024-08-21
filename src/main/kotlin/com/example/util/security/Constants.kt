package com.example.util

object Constants {

    const val DATABASE_NAME = "educo"
    const val DEFAULT_PAGE_SIZE =20
    const val DEFAULT_ACTIVITY_PAGE_SIZE = 15

    const val MAX_COMMENT_LENGTH = 2000

    const val BASE_URL = "http://192.168.0.118:8080/"
    const val PROFILE_PICTURE_PATH = "build/resources/main/static/profile_pictures/"

    const val DEFAULT_PROFILE_PICTURE_PATH = "${BASE_URL}/profile_pictures/avatar.svg"
    const val DEFAULT_BANNER_IMAGE_PATH = "${BASE_URL}/profile_pictures/banner.png"
    const val SMTP_SERVER_HOST = "smtp.gmail.com" // host for gmail smtp
    const val SMTP_SERVER_PORT = 587 // since we are using TLS
    const val SMTP_SERVER_USER_NAME = "sanskarbhadani11@gmail.com" //test@gmail.com
    const val SMTP_SERVER_PASSWORD = "qtgf leod vrpd hruc" // your 16 digit app password
    const val EMAIL_FROM = "sanskarbhadani11@gmail.com" // test@gmail.com

}