package com.example.carapp.Apis

object TestApi{
  // Bucket URL
  const val Bucket_Upload = "https://mexemai.com/bucket/upload"
  // BASE URL
    const val Base_url = "http://192.168.1.17:7000"
//    const val Base_url = "https://sabac-server-530056698.us-central1.run.app"

    //GUEST API
    const val post_guest_details = "$Base_url/post_guest_details/"
    const val guest_add_car_details = "$Base_url/guest_add_car_details/"
    const val assign_inspector_to_car = "$Base_url/assign_inspector_to_car/"


    // SELLER API
    const val Get_Inspector = "$Base_url/get_inspectors/"
    const val Get_Logout = "$Base_url/logout/"
    const val Get_Inspector_slots = "$Base_url/get_free_slots/"
    const val Get_Login = "$Base_url/login/"
    const val Get_Register = "$Base_url/saler_register/"
    const val Post_Add = "$Base_url/add_car_details/"
    const val Post_UserInfo = "$Base_url/post_additional_details/"
    const val Post_BookSlot = "$Base_url/select_slot/"
    const val get_user_cars = "$Base_url/get_user_cars/"
    const val Bid_notification_for_seller = "$Base_url/Bid_notification_for_seller/"
    const val mark_bid_notifications_as_read = "$Base_url/mark_bid_notifications_as_read/"
    const val accept_bid = "$Base_url/accept_bid/"
    const val reject_bid = "$Base_url/reject_bid/"
    const val update_is_booked = "$Base_url/update_is_booked/"


    //INSPECTOR API
    const val Get_Inspector_cars = "$Base_url/inspector_appointments/"
    const val Post_manual_entry = "$Base_url/assign-slot/"
    const val Get_assigned_slots = "$Base_url/get_assigned_slots/"
    const val add_availability = "$Base_url/add_availability/"
    const val post_inspection_report = "$Base_url/post_inspection_report_mob/"
    const val get_inspection_report = "$Base_url/get_inspection_report/"
    const val update_status = "$Base_url/update_status/"
    const val update_status_inspection = "$Base_url/mark-inspected/"
    const val get_guest_car_details = "$Base_url/get_guest_car_details/"
    const val assign_slot = "$Base_url/assign_slot/"
    const val seller_manual_entries = "$Base_url/seller_manual_entries/"


    //DEALER API
    const val get_upcoming_cars = "$Base_url/get_upcoming_cars/"
    const val get_bidding_cars = "$Base_url/get_bidding_cars/"
    const val place_bid = "$Base_url/place_bid/"

  //ADMIN API
  const val Get_Register_admin = "$Base_url/admin_register/"
  const val Get_Register_dealer = "$Base_url/dealer_register/"
  const val Get_Register_inspector = "$Base_url/inspector_register/"
  const val Get_Dealer_list = "$Base_url/dealers/"
  const val Get_Inspector_list = "$Base_url/inspectors/"
  const val Get_Admin_list = "$Base_url/adminList/"
  const val Get_dealer_update = "$Base_url/dealer-update/"
  const val Get_inspector_update = "$Base_url/dealer-update/"
  const val Get_admin_update = "$Base_url/dealer-update/"
  const val delete_user = "$Base_url/delete_user/"
  const val get_approval_list = "$Base_url/get_cars_for_approval/"
  const val post_accept = "$Base_url/approve_inspection/"
  const val Post_reject = "$Base_url/reject_inspection/"

}

