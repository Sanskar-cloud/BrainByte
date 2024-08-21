package com.example.di


import com.example.data.repository.PaymentRepo
import com.example.data.repository.PaymentRepoImpl
import com.example.data.repository.activity.ActivityRepository
import com.example.data.repository.activity.ActivityRepositoryImpl
import com.example.data.repository.aws.AmazonFileStorage
import com.example.data.repository.aws.FileStorage
import com.example.data.repository.category.CategoryRepository
import com.example.data.repository.category.CategoryRepositoryImpl
import com.example.data.repository.comment.CommentRepository
import com.example.data.repository.comment.CommentRepositoryImpl
import com.example.data.repository.course.CourseRepository
import com.example.data.repository.course.CourseRepositoryImpl
import com.example.data.repository.email.MainRepo
import com.example.data.repository.email.MainRepoImpl
import com.example.data.repository.feedback.FeedbackRepository
import com.example.data.repository.feedback.FeedbackRepositoryImpl
import com.example.data.repository.follow.FollowRepository
import com.example.data.repository.follow.FollowRepositoryImpl
import com.example.data.repository.lesson.LessonRepository
import com.example.data.repository.lesson.LessonRepositoryImpl
import com.example.data.repository.like.LikeRepository
import com.example.data.repository.like.LikeRepositoryImpl
import com.example.data.repository.project.ProjectRepository
import com.example.data.repository.project.ProjectRepositoryImpl
import com.example.data.repository.resource.ResourceRepository
import com.example.data.repository.resource.ResourceRepositoryImpl
import com.example.data.repository.user.UserRepository
import com.example.data.repository.user.UserRepositoryImpl
import com.example.data.responses.PaymentLinkResponse
import com.example.service.*
import com.example.util.Constants
import com.example.util.Constants.DATABASE_NAME
import com.example.util.security.hashing.HashingService
import com.example.util.security.hashing.SHA256HashingService
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.netty.util.Constant
import org.koin.dsl.module
import org.simplejavamail.api.mailer.Mailer
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.mailer.MailerBuilder


fun getKoinModule() = module {

    single <MongoDatabase>{
        MongoClient.create("mongodb+srv://sanskarbhadani11:gzUCySkLqM7hKyyR@cluster1.o7wkinf.mongodb.net/?retryWrites=true&w=majority&appName=Cluster1")

            .getDatabase(DATABASE_NAME)

    }
//
    single<FileStorage> {
        AmazonFileStorage()
    }
    single<MainRepo>{
        MainRepoImpl(
            emailService = get<EmailService>()
        )
    }
    single<EmailService> {
        DefaultEmailService(
            mailer = get<Mailer>()
        )
    }
    single<Mailer> {
        MailerBuilder
            .withSMTPServer(Constants.SMTP_SERVER_HOST, Constants.SMTP_SERVER_PORT)
            .withTransportStrategy(TransportStrategy.SMTP_TLS)
            .withSMTPServerUsername(Constants.SMTP_SERVER_USER_NAME)
            .withSMTPServerPassword(Constants.SMTP_SERVER_PASSWORD)
            .buildMailer()


    }

    /*Repository Instance*/
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single<CommentRepository> {
        CommentRepositoryImpl(get())
    }
    single<CourseRepository> {
        CourseRepositoryImpl(get())
    }
    single<LessonRepository> {
        LessonRepositoryImpl(get())
    }
    single<ResourceRepository> {
        ResourceRepositoryImpl(get())
    }
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single<PaymentRepo> {
        PaymentRepoImpl(get())
    }


    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single<FeedbackRepository> {
        FeedbackRepositoryImpl(get())
    }
    single<ProjectRepository> {
        ProjectRepositoryImpl(get())
    }
    single<CategoryRepository> {
        CategoryRepositoryImpl(get())
    }


    /*Service Instance*/

    single { CourseService(get()) }

    single { LessonService(get()) }
    single { ResourceService(get()) }
    single { FollowService(get()) }
    single { ActivityService(get(), get(), get()) }
    single { CommentService(get(), get()) }
    single { LikeService(get(), get(), get()) }
    single { FeedBackService(get()) }
    single { UserService(get(), get()) }
    single { ProjectService(get()) }
    single { CategoryService(get()) }
    single { PaymentService(get()) }
    single<HashingService> {
        SHA256HashingService()
    }


}