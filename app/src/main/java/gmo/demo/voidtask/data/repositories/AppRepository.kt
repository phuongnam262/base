package gmo.demo.voidtask.data.repositories

import gmo.demo.voidtask.data.entities.request.LoginRequest
import gmo.demo.voidtask.data.entities.request.VerifyOTPRequest
import gmo.demo.voidtask.data.entities.responses.BaseResponse
import gmo.demo.voidtask.data.entities.responses.ConsumerLoginResponse
import gmo.demo.voidtask.data.network.SafeApiRequest
import gmo.demo.voidtask.data.network.services.AppServives

class AppRepository (
    private val api: AppServives
) : SafeApiRequest() {

    suspend fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): BaseResponse<ConsumerLoginResponse> {
        return apiRequest { api.verifyOTP(verifyOTPRequest) }
    }

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body()?.data?.token != null) {
                // Lưu token nếu cần: PreferenceHelper.writeString(ConstantUtils.API_TOKEN, response.body()?.data?.token)
                Result.success(true)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}