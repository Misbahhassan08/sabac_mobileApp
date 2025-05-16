package com.example.carapp.screens.Dealer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.Apis.TestApi
import com.example.carapp.screens.getToken
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
class   ViewBidModel : ViewModel() {
    private val _report = MutableStateFlow<Report?>(null)
    val report: StateFlow<Report?> = _report
    private val _reportList = mutableStateOf<List<Report>>(emptyList())
    val reportList: State<List<Report>> = _reportList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    /*
    fun fetchReport(carId: String, context: Context) {
        _isLoading.value = true

        val url = "${TestApi.get_inspection_report}?car_id=$carId"
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ViewReportModel", "API call failed: ${e.message}")
                e.printStackTrace()
                viewModelScope.launch { _isLoading.value = false }
            }
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("Response", "FULL RESPONSE:\n$responseBody")
                viewModelScope.launch {
                    _isLoading.value = false
                    responseBody?.let {
                        try {
                            val type = object : TypeToken<List<ReportResponse>>() {}.type
                            val parsedReports: List<ReportResponse> = Gson().fromJson(it, type)
                            _reportList.value = parsedReports.map { report ->
                                Log.d("tech", "@@ ${report.jsonObj.carDetail.techSpecs}")
                                Report(
                                    id = report.id,
                                    carName = report.jsonObj.carDetail.basicInfo.carName,
                                    carModel = report.jsonObj.carDetail.basicInfo.carModel,
                                    bodyColor = report.jsonObj.carDetail.basicInfo.bodyColor,
                                    company = report.jsonObj.carDetail.basicInfo.company,
                                    condition = report.jsonObj.carDetail.techSpecs.condition,
                                    assembly = report.jsonObj.carDetail.techSpecs.assembly,
                                    engineCapacity = report.jsonObj.carDetail.techSpecs.engineCapacity,
                                    fuelType = report.jsonObj.carDetail.techSpecs.fuelType,
                                    kmsDriven = report.jsonObj.carDetail.techSpecs.kmsDriven,
                                    variant = report.jsonObj.carDetail.techSpecs.variant,
                                )
                            }

                            _report.value = _reportList.value.firstOrNull()
                        } catch (e: Exception) {
                            Log.e("ViewReportModel2", "Parsing error: ${e.message}")
                        }
                    }
                }
            }

        })
    }
*/
    fun fetchReport(carId: String, context: Context) {
        _isLoading.value = true

        val url = "${TestApi.get_inspection_report}?car_id=$carId"
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply {
                token?.let { addHeader("Authorization", "Bearer $it") }
            }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ViewReportModel", "API call failed: ${e.message}")
                e.printStackTrace()
                viewModelScope.launch { _isLoading.value = false }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("Response", "FULL RESPONSE:\n$responseBody")

                viewModelScope.launch {
                    _isLoading.value = false

                    responseBody?.let {
                        try {
                            val parsedReport: ReportResponse =
                                Gson().fromJson(it, ReportResponse::class.java)

                            _report.value = Report(
                                id = parsedReport.id,
                                carName = parsedReport.jsonObj.carDetail.basicInfo.carName,
                                carModel = parsedReport.jsonObj.carDetail.basicInfo.carModel,
                                bodyColor = parsedReport.jsonObj.carDetail.basicInfo.bodyColor,
                                company = parsedReport.jsonObj.carDetail.basicInfo.company,
                                condition = parsedReport.jsonObj.carDetail.techSpecs.condition,
                                assembly = parsedReport.jsonObj.carDetail.techSpecs.assembly,
                                engineCapacity = parsedReport.jsonObj.carDetail.techSpecs.engineCapacity,
                                fuelType = parsedReport.jsonObj.carDetail.techSpecs.fuelType,
                                kmsDriven = parsedReport.jsonObj.carDetail.techSpecs.kmsDriven,
                                variant = parsedReport.jsonObj.carDetail.techSpecs.variant
                            )

                        } catch (e: Exception) {
                            Log.e("ViewReportModel2", "Parsing error: ${e.message}")
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
    }
}

data class Report(
    val id: Int,
    val carName: String,
    val carModel: String,
    val bodyColor: String,
    val company: String,
    val condition: String,
    val assembly: String,
    val engineCapacity: String,
    val fuelType: String,
    val kmsDriven: String,
    val variant: String
)

data class ReportWrapper(
    val status: Boolean,
    val data: List<ReportResponse>
)

data class ReportResponse(
    val id: Int,
    @SerializedName("json_obj") val jsonObj: JsonObj
)

data class JsonObj(
    @SerializedName("Car Detail") val carDetail: CarDetail
)

data class CarDetail(
    val basicInfo: BasicInfo,
    val techSpecs: TechSpecs,
    val bodyParts: Map<String, BodyPartDetail>? = null
)

data class BasicInfo(
    val bodyColor: String,
    val carModel: String,
    val carName: String,
    val company: String
)

data class TechSpecs(
    val assembly: String,
    val condition: String,
    val engineCapacity: String,
    val fuelType: String,
    val kmsDriven: String,
    val variant: String
)

data class BodyPartDetail(
    val condition: String?,
    val image: String?
)




/*

data class Report(
    val carId: Int,
    val inspectorId: Int,
    val salerCarId: Int,
    val carName: String,
    val carCompany: String,
    val color: String,
    val condition: String,
    val model: String,
    val fuelType: String,
    val registryNumber: String,
    val engineCapacity: Double,
    val mileage: Double,
    val chassisNumber: String,
    val engineType: String,
    val transmissionType: String,
    val engineCondition: Int,
    val bodyCondition: Int,
    val clutchCondition: Int,
    val steeringCondition: Int,
    val suspensionCondition: Int,
    val brakesCondition: Int,
    val acCondition: Int,
    val electricalCondition: Int,
    val estimatedValue: String,
    val salerDemand: String,
    val overallScore: Int,
    val inspectionDate: String,
    val car_photos: List<String>,

    // Seller details
    val sellerId: Int,
    val sellerUsername: String,
    val sellerFirstName: String,
    val sellerLastName: String,
    val sellerEmail: String,
    val sellerPhone: String
)

*/








/*

class   ViewReportModel : ViewModel() {
    private val _report = MutableStateFlow<Report?>(null)
    val report: StateFlow<Report?> = _report
    private val _reportList = mutableStateOf<List<Report>>(emptyList())
    val reportList: State<List<Report>> = _reportList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    /*
    fun fetchReport(carId: String, context: Context) {
        _isLoading.value = true

        val url = "${TestApi.get_inspection_report}?car_id=$carId"
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ViewReportModel", "API call failed: ${e.message}")
                e.printStackTrace()
                viewModelScope.launch { _isLoading.value = false }
            }

          */
    /* override fun onResponse(call: Call, response: Response) {
               val responseBody = response.body?.string()
               Log.d("ViewReportModel", "Response Code: ${response.code}")
               Log.d("ViewReportModel", "Response Body: $responseBody")

               if (response.isSuccessful) {
                   try {
                       responseBody?.let {
                           val jsonArray = JSONArray(it)

                           if (jsonArray.length() > 0) {
                               val carJson = jsonArray.getJSONObject(0) // First object
                               val userJson = carJson.getJSONObject("user")

                               val photosArray = carJson.getJSONArray("car_photos")
                               val carPhotosList = mutableListOf<String>()
                               for (j in 0 until photosArray.length()) {
                                   carPhotosList.add(photosArray.getString(j))
                               }

                               val report = Report(
                                   carId = carJson.getInt("id"),
                                   inspectorId = carJson.getInt("inspector"),
                                   salerCarId = carJson.getInt("saler_car"),
                                   carName = carJson.getString("car_name"),
                                   carCompany = carJson.getString("company"),
                                   color = carJson.getString("color"),
                                   condition = carJson.getString("condition"),
                                   model = carJson.getString("model"),
                                   fuelType = carJson.getString("fuel_type"),
                                   registryNumber = carJson.getString("registry_number"),
                                   engineCapacity = carJson.getDouble("engine_capacity"),
                                   mileage = carJson.getDouble("mileage"),
                                   chassisNumber = carJson.getString("chassis_number"),
                                   engineType = carJson.getString("engine_type"),
                                   transmissionType = carJson.getString("transmission_type"),
                                   engineCondition = carJson.getInt("engine_condition"),
                                   bodyCondition = carJson.getInt("body_condition"),
                                   clutchCondition = carJson.getInt("clutch_condition"),
                                   steeringCondition = carJson.getInt("steering_condition"),
                                   suspensionCondition = carJson.getInt("suspension_condition"),
                                   brakesCondition = carJson.getInt("brakes_condition"),
                                   acCondition = carJson.getInt("ac_condition"),
                                   electricalCondition = carJson.getInt("electrical_condition"),
                                   estimatedValue = carJson.getString("estimated_value"),
                                   salerDemand = carJson.getString("saler_demand"),
                                   overallScore = carJson.getInt("overall_score"),
                                   inspectionDate = carJson.getString("inspection_date"),
                                   car_photos = carPhotosList,
                                   sellerId = userJson.getInt("id"),
                                   sellerUsername = userJson.getString("username"),
                                   sellerFirstName = userJson.getString("first_name"),
                                   sellerLastName = userJson.getString("last_name"),
                                   sellerEmail = userJson.getString("email"),
                                   sellerPhone = userJson.getString("phone_number")
                               )

                               viewModelScope.launch {
                                   _report.value = report
                                   _isLoading.value = false
                               }
                           } else {
                               Log.e("ViewReportModel", "Empty response array")
                               viewModelScope.launch { _isLoading.value = false }
                           }
                       }
                   } catch (e: Exception) {
                       Log.e("ViewReportModel", "Error parsing JSON: ${e.message}")
                       viewModelScope.launch { _isLoading.value = false }
                   }
               } else {
                   Log.e("ViewReportModel", "API request failed with code: ${response.code}")
                   viewModelScope.launch { _isLoading.value = false }
               }
           }*/
    /*

          override fun onResponse(call: Call, response: Response) {
              val responseBody = response.body?.string()
              Log.d("ViewReportModel", "Response Code: ${response.code}")
              Log.d("ViewReportModel", "Response Body: $responseBody")

              if (response.isSuccessful) {
                  try {
                      responseBody?.let {
                          val jsonArray = JSONArray(it)

                          if (jsonArray.length() > 0) {
                              val carJson = jsonArray.getJSONObject(0)

                              // Check if "user" field is null
                              val userJson = if (carJson.isNull("user")) null else carJson.getJSONObject("user")

                              val photosArray = carJson.getJSONArray("car_photos")
                              val carPhotosList = mutableListOf<String>()
                              for (j in 0 until photosArray.length()) {
                                  carPhotosList.add(photosArray.getString(j))
                              }

                              val report = Report(
                                  carId = carJson.getInt("id"),
                                  inspectorId = carJson.getInt("inspector"),
                                  salerCarId = carJson.getInt("saler_car"),
                                  carName = carJson.getString("car_name"),
                                  carCompany = carJson.getString("company"),
                                  color = carJson.getString("color"),
                                  condition = carJson.getString("condition"),
                                  model = carJson.getString("model"),
                                  fuelType = carJson.getString("fuel_type"),
                                  registryNumber = carJson.getString("registry_number"),
                                  engineCapacity = carJson.getDouble("engine_capacity"),
                                  mileage = carJson.getDouble("mileage"),
                                  chassisNumber = carJson.getString("chassis_number"),
                                  engineType = carJson.getString("engine_type"),
                                  transmissionType = carJson.getString("transmission_type"),
                                  engineCondition = carJson.getInt("engine_condition"),
                                  bodyCondition = carJson.getInt("body_condition"),
                                  clutchCondition = carJson.getInt("clutch_condition"),
                                  steeringCondition = carJson.getInt("steering_condition"),
                                  suspensionCondition = carJson.getInt("suspension_condition"),
                                  brakesCondition = carJson.getInt("brakes_condition"),
                                  acCondition = carJson.getInt("ac_condition"),
                                  electricalCondition = carJson.getInt("electrical_condition"),
                                  estimatedValue = carJson.getString("estimated_value"),
                                  salerDemand = carJson.getString("saler_demand"),
                                  overallScore = carJson.getInt("overall_score"),
                                  inspectionDate = carJson.getString("inspection_date"),
                                  car_photos = carPhotosList,

                                  // Check if userJson is null before accessing its properties
                                  sellerId = userJson?.optInt("id") ?: 0,
                                  sellerUsername = userJson?.optString("username") ?: "N/A",
                                  sellerFirstName = userJson?.optString("first_name") ?: "N/A",
                                  sellerLastName = userJson?.optString("last_name") ?: "N/A",
                                  sellerEmail = userJson?.optString("email") ?: "N/A",
                                  sellerPhone = userJson?.optString("phone_number") ?: "N/A"
                              )

                              viewModelScope.launch {
                                  _report.value = report
                                  _isLoading.value = false
                              }
                          } else {
                              Log.e("ViewReportModel", "Empty response array")
                              viewModelScope.launch { _isLoading.value = false }
                          }
                      }
                  } catch (e: Exception) {
                      Log.e("ViewReportModel", "Error parsing JSON: ${e.message}")
                      viewModelScope.launch { _isLoading.value = false }
                  }
              } else {
                  Log.e("ViewReportModel", "API request failed with code: ${response.code}")
                  viewModelScope.launch { _isLoading.value = false }
              }
          }


        })
    }

    */

    fun fetchReport(carId: String, context: Context) {
        _isLoading.value = true

        val url = "${TestApi.get_inspection_report}?car_id=$carId"
        val token = getToken(context)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .apply { token?.let { addHeader("Authorization", "Bearer $it") } }
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ViewReportModel", "API call failed: ${e.message}")
                e.printStackTrace()
                viewModelScope.launch { _isLoading.value = false }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ViewReportModelR", "RAW RESPONSE: ${responseBody.toString()}")
                Log.d("ViewReportModel", "Response Code: ${response.code}")
                Log.d("ViewReportModel", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    try {
                        responseBody?.let {
                            val jsonArray = JSONArray(it)

                            if (jsonArray.length() > 0) {
                                val reportList = mutableListOf<Report>()

                                for (i in 0 until jsonArray.length()) {
                                    val item = jsonArray.getJSONObject(i)
                                    val report = Report(
                                        id = item.getInt("id"),
                                        inspectorId = item.getInt("inspector"),
                                        salerCarId = item.getInt("saler_car"),
                                        createdAt = item.getString("created_at"),
                                        isAccepted = item.getBoolean("is_accepted"),
                                        isRejected = item.getBoolean("is_rejected"),
                                        jsonObj = if (!item.isNull("json_obj")) {
                                            try {
                                                parseInspectionData(item.getJSONObject("json_obj"))
                                            } catch (e: Exception) {
                                                Log.e(
                                                    "ViewReportModel",
                                                    "Error parsing inspection data: ${e.message}"
                                                )
                                                null
                                            }
                                        } else null
                                    )
                                    reportList.add(report)
                                }

                                viewModelScope.launch {
                                    _reportList.value = reportList
                                    _isLoading.value = false
                                }
                            } else {
                                Log.e("ViewReportModel", "Empty response array")
                                viewModelScope.launch { _isLoading.value = false }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ViewReportModel", "Error parsing JSON: ${e.message}")
                        viewModelScope.launch { _isLoading.value = false }
                    }
                } else {
                    Log.e("ViewReportModel", "API request failed with code: ${response.code}")
                    viewModelScope.launch { _isLoading.value = false }
                }
            }
        })
    }
}

    private fun parseInspectionData(jsonObj: JSONObject): InspectionData? {
        Log.d("ViewReportModel", "Parsing JSON: ${jsonObj.toString()}") // Add this line
        return try {
            // Safely get the Car Detail object
            val carDetail = jsonObj.optJSONObject("Car Detail") ?: return null
            Log.d("ViewReportModelD", "CarDetail JSON: ${carDetail.toString(2)}")

            // Safely parse basicInfo with default empty strings
//            val basicInfoJson = carDetail.optJSONObject("basicInfo") ?: JSONObject()
            val basicInfoJson = carDetail.optJSONObject("basicInfo")?: JSONObject()
            Log.d("ViewReportModelB", "basicInfo: ${basicInfoJson?.toString(2)}")
            val basicInfo = BasicInfo(
                bodyColor = basicInfoJson.optString("bodyColor", ""),
                carModel = basicInfoJson.optString("carModel", ""),
                carName = basicInfoJson.optString("carName", ""),
                company = basicInfoJson.optString("company", "")
            )

            // Safely parse techSpecs with default empty strings
            val techSpecsJson = carDetail.optJSONObject("techSpecs") ?: JSONObject()
            val techSpecs = TechSpecs(
                assembly = techSpecsJson.optString("assembly", ""),
                condition = techSpecsJson.optString("condition", ""),
                engineCapacity = techSpecsJson.optString("engineCapacity", ""),
                fuelType = techSpecsJson.optString("fuelType", ""),
                kmsDriven = techSpecsJson.optString("kmsDriven", ""), // Note: Fixed typo from previous version
                variant = techSpecsJson.optString("variant", "")
            )

            // Safely parse body parts inspection
            val bodyParts = mutableMapOf<String, Map<String, Map<String, Int>>>()
            Log.d("VVV","LOG $bodyParts")
            val bodyPartsInspection = jsonObj.optJSONObject("Body Parts Inspection")
            Log.d("VVV","LOG $bodyPartsInspection")

            bodyPartsInspection?.let { inspection ->
                val carBodyOuter = inspection.optJSONObject("Car Body (Outer)")

                carBodyOuter?.let { outer ->
                    val iterator = outer.keys()
                    while (iterator.hasNext()) {
                        val partName = iterator.next()
                        val partData = outer.optJSONObject(partName) ?: continue

                        val partDetails = mutableMapOf<String, Map<String, Int>>()
                        val partIterator = partData.keys()
                        while (partIterator.hasNext()) {
                            val detailName = partIterator.next()
                            val detailData = partData.optJSONObject(detailName) ?: continue

                            val detailMap = mutableMapOf<String, Int>()
                            val detailValueIterator = detailData.keys()
                            while (detailValueIterator.hasNext()) {
                                val key = detailValueIterator.next()
                                detailMap[key] = detailData.optInt(key, 0)
                            }
                            partDetails[detailName] = detailMap
                        }
                        if (partDetails.isNotEmpty()) {
                            bodyParts[partName] = partDetails
                        }
                    }
                }
            }

            InspectionData(
                basicInfo = basicInfo,
                techSpecs = techSpecs,
                bodyParts = bodyParts
            )
        } catch (e: Exception) {
            Log.e("ViewReportModel", "Error parsing inspection data: ${e.message}")
            null
        }
    }


    // Data classes for the new structure
    data class Report(
        val id: Int,
        val inspectorId: Int,
        val salerCarId: Int,
        val createdAt: String,
        val isAccepted: Boolean,
        val isRejected: Boolean,
        val jsonObj: InspectionData? // Changed from JSONObject to our data class
    )

    data class InspectionData(
        val basicInfo: BasicInfo,
        val techSpecs: TechSpecs,
        val bodyParts: Map<String, Map<String, Map<String, Int>>>
    )

    data class BasicInfo(
        val bodyColor: String,
        val carModel: String,
        val carName: String,
        val company: String
    )

    data class TechSpecs(
        val assembly: String,
        val condition: String,
        val engineCapacity: String,
        val fuelType: String,
        val kmsDriven: String,
        val variant: String
    )


/*data class Report(
    val carId: Int,
    val inspectorId: Int,
    val salerCarId: Int,
    val carName: String,
    val carCompany: String,
    val color: String,
    val condition: String,
    val model: String,
    val fuelType: String,
    val registryNumber: String,
    val engineCapacity: Double,
    val mileage: Double,
    val chassisNumber: String,
    val engineType: String,
    val transmissionType: String,
    val engineCondition: Int,
    val bodyCondition: Int,
    val clutchCondition: Int,
    val steeringCondition: Int,
    val suspensionCondition: Int,
    val brakesCondition: Int,
    val acCondition: Int,
    val electricalCondition: Int,
    val estimatedValue: String,
    val salerDemand: String,
    val overallScore: Int,
    val inspectionDate: String,
    val car_photos: List<String>,

    // Seller details
    val sellerId: Int,
    val sellerUsername: String,
    val sellerFirstName: String,
    val sellerLastName: String,
    val sellerEmail: String,
    val sellerPhone: String
)*/
* */