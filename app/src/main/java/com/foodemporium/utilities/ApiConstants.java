package com.foodemporium.utilities;

public class ApiConstants {

    public static final String SP_Name = "FOODEMPORIUM";

    public static final String keyMobile_No = "MobileNo";
    public static final String keyPassword = "Password";

    public static final String CSTID = "CSTID";
    public static final String keyCstId = "CstId";

    public static final String BASEURL = "http://nafam.com.au/api/";

    public static final String GETSTATELIST = BASEURL + "GetStateList";

    public static final String GETCITYLIST = BASEURL + "GetCityList?stateid=";

    public static final String MEMBERREGISTER = BASEURL + "MemberRegister";

    public static final String GETCUISINETYPES = BASEURL + "GetCuisineTypes";

    public static final String GETMERCHANTLIST = BASEURL + "GetMerchantList?csid=2&cityid=1";

//    http://localhost:1213/api/GetMerchantList?csid=2&cityid=1

    public static final String GETMEMBERLOGIN = BASEURL + "getmemberlogin";

    public static final String GETPRODUCTSLIST = BASEURL + "GetProductsList?mid=";

    public static final String TABLEBOOKING = BASEURL + "TableBooking";


    public static final String GETMERCHANTTIMINGSBYDATE = BASEURL + "GetMerchantTimingsByDate?mid=";

//    http://nafam.com.au/api/GetMerchantTimingsByDate?mid=1&date=2020-05-20

    public static final String GETMERCHANTGUESTLIMIT = BASEURL + "GetMerchantGuestLimit?mid=";


//    {
//         "Username":"anil@gmail.com",
//            "Password":"1996",
//            "DeviceId":"894758947589",
//            "AppType":1
//    }

}
