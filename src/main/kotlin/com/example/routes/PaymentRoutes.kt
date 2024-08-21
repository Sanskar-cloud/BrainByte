package com.example.routes


import com.example.data.models.Payment
import com.example.data.models.PaymentStatus
import com.example.data.models.StudentPayment
import com.example.data.requests.PaymentRequest
import com.example.data.requests.PaymentRequestStudent
import com.example.data.responses.BasicApiResponse
import com.example.data.responses.PaymentLinkResponse
import com.example.service.CourseService
import com.example.service.PaymentService
import com.example.service.UserService
import com.razorpay.RazorpayClient
import com.razorpay.RazorpayException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.coroutines.flow.singleOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


fun Route.createPayment(paymentService: PaymentService) {
    post("/createPayment") {
        val paymentRequest = call.receive<PaymentRequest>()
        val apiKey = System.getenv("RAZORPAY_API_KEY")
        val apiSecret = System.getenv("RAZORPAY_SECRET_KEY")

        try {
            val razorpay = RazorpayClient(apiKey, apiSecret)
            val paymentLinkRequest = JSONObject().apply {
                put("amount",  1* 100) // Amount in paise
                put("currency", "INR")
                put("customer", JSONObject().apply {
                    put("name", paymentRequest.teacherName)

                    put("email", paymentRequest.teacherEmail)
                })
                put("notify", JSONObject().apply {

                    put("email", true)
                })
                put("options", JSONObject().apply {
                    put("payment_method", JSONObject().apply {
                        put("upi", true)
                    })
                })
                put("reminder_enable", true)
                put("callback_url", "https://2213-202-168-86-106.ngrok-free.app/api/paymentCallback")
                put("callback_method", "get")
            }

            val paymentLink = razorpay.paymentLink.create(paymentLinkRequest)
            val paymentLinkId = paymentLink.get("id") as String
            val paymentLinkUrl = paymentLink.get("short_url") as String
            println(paymentLinkUrl+"yfujrcjytgrn6u6trhgnedtrhx")
            println(paymentLink)
            println(paymentLinkId)



            val payment = Payment(
                teacherId = paymentRequest.teacherId,
                amount = 1,
                paymentLinkid = paymentLinkId,

                currency = "INR"

            )
            paymentService.savePayment(payment)

            call.respond(HttpStatusCode.Accepted, PaymentLinkResponse(paymentLinkUrl, paymentLinkId))
            println(PaymentLinkResponse)
        } catch (e: RazorpayException) {
            call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error creating payment link")
        }
    }

}
fun Route.createPaymentStudent(paymentService: PaymentService,courseService:CourseService) {
    post("/createPaymentStudent") {
        val paymentRequest = call.receive<PaymentRequestStudent>()
        val apiKey = System.getenv("RAZORPAY_API_KEY")
        val apiSecret = System.getenv("RAZORPAY_SECRET_KEY")

        val course = courseService.getCourseById(paymentRequest.courseId)
        val coursePriceString = course?.price ?: throw IllegalArgumentException("Course not found")
        val coursePrice = coursePriceString.toInt()
        val amountInPaise = coursePrice * 100
        try {
            val razorpay = RazorpayClient(apiKey, apiSecret)
            val paymentLinkRequest = JSONObject().apply {
                put("amount",  amountInPaise) // Amount in paise
                put("currency", "INR")
                put("customer", JSONObject().apply {
                    put("name", paymentRequest.StudentName)

                    put("email", paymentRequest.StudentEmail)
                })
                put("notify", JSONObject().apply {

                    put("email", true)
                })
                put("options", JSONObject().apply {
                    put("payment_method", JSONObject().apply {
                        put("upi", true)
                    })
                })
                put("reminder_enable", true)
                put("callback_url", "https://2213-202-168-86-106.ngrok-free.app/api/paymentCallbackStudent")
                put("callback_method", "get")
            }

            val paymentLink = razorpay.paymentLink.create(paymentLinkRequest)
            val paymentLinkId = paymentLink.get("id") as String
            val paymentLinkUrl = paymentLink.get("short_url") as String
            println(paymentLinkUrl+"yfujrcjytgrn6u6trhgnedtrhx")
            println(paymentLink)
            println(paymentLinkId)



            val payment = StudentPayment(
                StudentId = paymentRequest.StudentId,
                amount = coursePrice,
                paymentLinkid = paymentLinkId,
                courseId = paymentRequest.courseId,

                currency = "INR"

            )
            paymentService.savePaymentStudent(payment)

            call.respond(HttpStatusCode.Accepted, PaymentLinkResponse(paymentLinkUrl, paymentLinkId))
            println(PaymentLinkResponse)
        } catch (e: RazorpayException) {
            println("fkctiydfymyg,kyufkuyfkyudfukfhjftudtyfghcdtyftdfytjfrxdtyfgjfdtfgjkhlfdhfgujfh ${e.message}")
            call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error creating payment link")
        }
    }

}
fun Route.callback(paymentService: PaymentService) {
    post("/api/paymentCallback") {

        val callbackRequest = call.receiveOrNull<String>()
        if (callbackRequest == null) {
            call.respond(HttpStatusCode.BadRequest, "Request body is missing")
            return@post
        }
        val a=callbackRequest

        println("Payment callback received: $callbackRequest")
        try {
            val jsonObject = JSONObject(callbackRequest)
            val accountId = jsonObject.getString("account_id")
            val event = jsonObject.getString("event")
            val payload = jsonObject.getJSONObject("payload")
            val paymentLinkId = payload.getJSONObject("payment_link").getJSONObject("entity").getString("id")


            val paymentLink = payload.getJSONObject("payment_link").getJSONObject("entity")
            val paymentStatus = payload.getJSONObject("payment").getJSONObject("entity").getString("status") as String


            val paymentId = paymentLink.getString("id")
            val orderId = paymentLink.getString("order_id")
            val amount = paymentLink.getInt("amount")
            val currency = paymentLink.getString("currency")
            var paymentRecord = paymentService.findPaymentByOrderId(paymentLinkId).singleOrNull()
            println("AEFFSKJLK/HGJDHFXCHVJKLHKGJHXBCKHGJBXFHJGKHGHVCBHJGKXFCHJGKBV NVMNV,GHFCBV BM  ,H VYJ,HCV,JC HFVJ, VHF,HJ NBCNMVH JHVC GH,JFVFYLI,VCKHGCVJLH,GKJ;O.LHKHIOLHGVFY,JHMV" +
                    "HGL,KG LK,GFGCMHJVCKTUG,HJFYULITGIL.LFYLUYUL,JFLI,FJHKDYTKHM NFKKYU,UKGTFVLUDFTC,JHXJTRFGCH,JFBHLJKXF,FJ $paymentRecord")
            if (paymentRecord == null) {
                call.respond(HttpStatusCode.NotFound, "Payment not found")
                return@post}

            if (paymentStatus == "captured") {



                paymentRecord.status = PaymentStatus.COMPLETED
                paymentService.updatePaymentStatus(paymentLinkId,PaymentStatus.COMPLETED)


                call.respond(HttpStatusCode.OK, "Payment successful, you can create the course now")
            } else {
                call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false, "Payment Not Done", paymentRecord))
            }
            println("mvhl j,yutc,yfj lhf,gyuykfk $paymentStatus")}




//        val callbackPayload = call.receive<Map<String, String>>()
//        val parameters = call.receiveParameters()
//        val paymentId = callbackPayload["razorpay_payment_id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing payment ID")
//        val orderId = callbackPayload["razorpay_order_id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing order ID")
//        val paymentLinkId = callbackPayload["razorpay_payment_link_id"]
//        val paymentLinkStatus = callbackPayload["razorpay_payment_link_status"]
//        val razorpaySignature = callbackPayload["razorpay_signature"]
//
//        if (paymentId == null || orderId == null || paymentLinkId == null || paymentLinkStatus == null || razorpaySignature == null) {
//            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
//            return@post
//        }
//
//        val apiKey = System.getenv("RAZORPAY_API_KEY")
//        val apiSecret = System.getenv("RAZORPAY_API_SECRET")
//
//        try {
//            val razorpay = RazorpayClient(apiKey, apiSecret)
//            val payment = razorpay.payments.fetch(paymentId)
//            println(payment)
//            var paymentRecord = paymentService.findPaymentByOrderId(orderId).singleOrNull()
//            if (paymentRecord == null) {
//                call.respond(HttpStatusCode.NotFound, "Payment not found")
//                return@post
//            }
//            if (payment.get<String>("status") == "captured") {
//                paymentRecord.id = paymentId
//                paymentRecord.status = PaymentStatus.COMPLETED
//                paymentService.savePayment(paymentRecord)
//                call.respond(HttpStatusCode.OK, "Payment successful, you can create the course now")
//            } else {
//                call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false, "Payment Not Done", paymentRecord))
//            }
//        }
//        }
        catch (e: RazorpayException) {
            call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error processing payment")
        }
    }}
@OptIn(InternalAPI::class)
fun Route.callbackStudent(paymentService: PaymentService, userService: UserService, courseService: CourseService) {
    post("/api/paymentCallbackStudent") {

        val callbackRequest = call.receiveOrNull<String>()
        if (callbackRequest == null) {
            call.respond(HttpStatusCode.BadRequest, "Request body is missing")
            return@post
        }

        println("Payment callback received FOR STUDENT: $callbackRequest")
        try {
            val jsonObject = JSONObject(callbackRequest)
            val accountId = jsonObject.getString("account_id")
            val event = jsonObject.getString("event")
            val payload = jsonObject.getJSONObject("payload")
            val paymentLinkId = payload.getJSONObject("payment_link").getJSONObject("entity").getString("id")
            val paymentStatus = payload.getJSONObject("payment").getJSONObject("entity").getString("status") as String

            val paymentRecord = paymentService.findPaymentByOrderIdStudent(paymentLinkId).singleOrNull()
            println("Payment record: $paymentRecord")

            if (paymentRecord == null) {
                call.respond(HttpStatusCode.NotFound, "Payment not found")
                return@post
            }

            if (paymentStatus == "captured") {

                paymentRecord.status = PaymentStatus.COMPLETED
                paymentService.updatePaymentStatusForTchr(paymentLinkId, PaymentStatus.COMPLETED)
                val course= paymentRecord.courseId?.let { it1 -> courseService.getCourseById(it1) }
                val tid= course?.courseTeacher?.Teacherid
                val user= tid?.let { it1 -> userService.getUserById(it1) }
                val accountDetails= user?.accountDetails
                paymentRecord.courseId?.let { it1 -> courseService.enrollCourse(it1,paymentRecord.StudentId,application) }



                val accountHolderName = accountDetails?.accountHolderName
                val accountNumber = accountDetails?.accountNumber
                val ifscCode = accountDetails?.ifscCode


                val apiKey = System.getenv("RAZORPAY_API_KEY")
                val apiSecret = System.getenv("RAZORPAY_SECRET_KEY")
                val razorpay = RazorpayClient(apiKey, apiSecret)
//                val client = HttpClient(CIO) {
//                    install(Auth) {
//                        basic {
//                            credentials {
//                                BasicAuthCredentials(username = apiKey, password = apiSecret)
//                            }
//                            realm = "Razorpay API"
//                        }
//                    }
//                    install(ContentNegotiation) {
//                        json(Json {
//                            prettyPrint = true
//                            isLenient = true
//                            ignoreUnknownKeys = true
//                        })
//                    }
//                }



                val customerRequest = JSONObject()
                customerRequest.put("name", "Gaurav Kumar")
                customerRequest.put("contact", "9123456780")
                customerRequest.put("email", "gaurav.kumar@example.com")
                customerRequest.put("fail_existing", "0")
                val notes = JSONObject()
                notes.put("notes_key_1", "Tea, Earl Grey, Hot")
                notes.put("notes_key_2", "Tea, Earl Grey… decaf.")
                customerRequest.put("notes", notes)

                val customer = razorpay.customers.create(customerRequest)
                val customerId= customer.get<String>("id")

//                val client = OkHttpClient()
//                val mediaType = "application/json".toMediaType()
//                val body = "{\n  \"name\":\"Gaurav Kumar\",\n  \"email\":\"gaurav.kumar@example.com\",\n  \"contact\":\"9123456780\",\n  \"fail_existing\":\"1\",\n  \"gstin\":\"12ABCDE2356F7GH\",\n  \"notes\":{\n    \"notes_key_1\":\"Tea, Earl Grey, Hot\",\n    \"notes_key_2\":\"Tea, Earl Grey… decaf.\"\n  }\n}".toRequestBody(mediaType)
//                val request = Request.Builder()
//                    .url("https://api.razorpay.com/v1/customers")
//                    .post(body)
//                    .addHeader("Content-Type", "application/json")
//                    .build()
//                val response = client.newCall(request).execute()

                // Create contact
//                val contactResponse: String = client.post("https://api.razorpay.com/v1/contacts") {
//                    contentType(ContentType.Application.Json)
//                    setBody(JSONObject().apply {
//                        put("name", accountHolderName)
//                        put("contact", "9123456789") // Provide a valid contact number
//                        put("type", "customer")
//                        put("email", user?.email)
//                        put("reference_id", tid)
//                    }.toString())
//                }.toString()


                // Create fund account
                val fundAccountRequest = JSONObject().apply {
                    put("customer_id", customerId)
                    put("account_type", "bank_account")
                    put("bank_account", JSONObject().apply {
                        put("name", accountHolderName)
                        put("ifsc", ifscCode)
                        put("account_number", accountNumber)
                    })
                }

//                val fundAccountRequest = JSONObject().apply {
//                    put("contact", JSONObject().apply {
//                        put("name", accountHolderName)
//                        put("contact", "9123456789") // Provide a valid contact number
//                        put("type", "customer")
//                        if (user != null) {
//                            put("email", user.email)
//                        }
//                        put("reference_id", tid)
//                    })
//                    put("account_type", "bank_account")
//                    put("bank_account", JSONObject().apply {
//                        put("name", accountHolderName)
//                        put("ifsc", ifscCode)
//                        put("account_number", accountNumber)
//                    })
//                }

                val fundAccount = razorpay.fundAccount.create(fundAccountRequest)
                val fundAccountId = fundAccount.get<String>("id")

                val transferRequest = JSONObject().apply {
                    put("amount", paymentRecord.amount * 100) // Amount in paise
                    put("currency", "INR")
                    put("account", "acc_OUFXFpRPIJPssl")
                }
                println("dsvdskvnervdsilbfverv viafjbvjfvi'F OAFVFOBGOFDJ;VANLSDKHFV'DSOBVAERFVBOFBVVfvdsvv $transferRequest")

                try {
                    val transfer = razorpay.transfers.create(transferRequest)
                    println(transfer)
                    println("Transfer successful FVBFGWFVFVF fgv GIFBVGFRVEFWGVBFVWFBBWFVWF;BGRVFSVBW;FGWF;VBVGFRBBNFBVRIVUBFVIRVBFVGIBLSFLBVDFSBVFIGIFIVBFVIBLFV;BV;BVPSFBDF;VBFV: ${transfer.toString()}")
                } catch (e: RazorpayException) {
                    println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                            "FIDV;SBHVNZDV GFBOH;VBVLHDVDFLVFSB ;LFJBVFHVJ DFSLBV;D B FVFSBLFKV.H S;BHV" +
                            "J FB R;FVZ DBHBS;HBRP;BVH SVIFKJVFHS;R VDFHGBGR B;B GR;OB F  ${e.message}")
                    call.respond(HttpStatusCode.InternalServerError, "Error transferring funds to teacher: ${e.message}")
                }



                // Get the teacher's account details

            } else {
                call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false, "Payment Not Done", paymentRecord))
            }
            println("Payment status: $paymentStatus")
        } catch (e: RazorpayException) {
            println("fgff DSFNDFVFBVFSBVF VSFGVNDJVFBVDLBBFV FHVDSJGKN;B;SFB; VFVBHVBVHLADVJLBSBV VBFIDV;SBHVNZDV GFBOH;VBVLHDVDFLVFSB ;LFJBVFHVJ DFSLBV;D B FVFSBLFKV.H S;BHV" +
                    "J FB R;FVZ DBHBS;HBRP;BVH SVIFKJVFHS;R VDFHGBGR B;B GR;OB F  ${e.message}")

            call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error processing payment")
        }
    }
}


//fun Route.callback(paymentService: PaymentService) {
//    post("/api/paymentCallback") {
//
//        val razorpayPaymentId = call.request.queryParameters["razorpay_payment_id"]
//        val razorpayPaymentLinkId = call.request.queryParameters["razorpay_payment_link_id"]
//        val razorpayPaymentLinkStatus = call.request.queryParameters["razorpay_payment_link_status"]
//        val razorpaySignature = call.request.queryParameters["razorpay_signature"]
//        val callbackRequest = call.receiveOrNull<String>()
//        if (callbackRequest == null) {
//            call.respond(HttpStatusCode.BadRequest, "Request body is missing")
//            return@post
//        }
//        val a=callbackRequest
//
//        println("Payment callback received: $callbackRequest")
//
//        try {
//            val jsonObject = JSONObject(callbackRequest)
//            val accountId = jsonObject.getString("account_id")
//            val event = jsonObject.getString("event")
//            val payload = jsonObject.getJSONObject("payload")
//            val paymentLinkId = payload.getJSONObject("payment_link").getJSONObject("entity").getString("id")
//
//
//            val paymentLink = payload.getJSONObject("payment_link").getJSONObject("entity")
//            val paymentStatus = payload.getJSONObject("payment").getJSONObject("entity").getString("status") as String
//
//            val paymentId = paymentLink.getString("id")
//            val orderId = paymentLink.getString("order_id")
//            val amount = paymentLink.getInt("amount")
//            val currency = paymentLink.getString("currency")
//            var paymentRecord = paymentService.findPaymentByOrderId(paymentLinkId).singleOrNull()
//
//        if (razorpayPaymentId == null || razorpayPaymentLinkId == null || razorpayPaymentLinkStatus == null || razorpaySignature == null) {
//            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
//            return@post
//        }
//
//        println("Payment callback received with GET: paymentLinkId: $razorpayPaymentLinkId, status: $razorpayPaymentLinkStatus")
//
//        try {
//            val paymentRecord = paymentService.findPaymentByOrderId(razorpayPaymentLinkId).singleOrNull()
//            if (paymentRecord == null) {
//                call.respond(HttpStatusCode.NotFound, "Payment not found")
//                return@post
//            }
//
//            if (paymentStatus == "captured") {
//                paymentService.updatePaymentStatus(razorpayPaymentLinkId, PaymentStatus.COMPLETED)
//
//            } else {
//                call.respond(HttpStatusCode.BadRequest, BasicApiResponse(false, "Payment Not Done", paymentRecord))
//            }
//            println("Payment status updated to: $razorpayPaymentLinkStatus")
//            if(razorpayPaymentLinkStatus=="paid"){
//                call.respond(HttpStatusCode.OK, "Payment successful, you can create the course now")
//            }
//
//        } catch (e: Exception) {
//            println("Error processing payment callback: ${e.message}")
//            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
//        }
//    }
//        catch (e: RazorpayException) {
//            call.respond(HttpStatusCode.InternalServerError, e.message ?: "Error processing payment")
//        }
//}}


// Create a payout

