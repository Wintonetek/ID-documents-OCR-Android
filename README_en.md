# ID-documents-OCR-Android
# Call for Preview Recognition
# Note: For the specific code call process, refer to the methods in AllCardMainActivity.java in the sample code.
# Jumping to the Camera Interface
# CardOcrRecogConfigure.getInstance()
# // Set the language for recognition results
# .initLanguage(getApplicationContext())
# // Whether to save the cropped image
# .setSaveCut(true)
# // ID of the main document type
# .setnMainId(SharedPreferencesHelper.getInt(
# getApplicationContext(), "nMainId", 2))
# // ID of the sub-document type
# .setnSubID( SharedPreferencesHelper.getInt(
# getApplicationContext(), "nSubID", 0))
# // Distinguish between front and back of ID card: 0 - Auto-distinguish; 1 - Only recognize the front; 2 - Only recognize the back (Note: Default is 0 if not set)
# .setFlag(0)
# // Set the scanning method: 0 - Guide frame scanning; 1 - Real-time edge detection
# .setnCropType(1)
# // Set the storage path of the image (Note: The default path is: Environment.getExternalStorageDirectory().toString()
# // + "/wtimage/")
# .setSavePath(new DefaultPicSavePath());
# Intent intent=new Intent(AllCardMainActivity.this,
# CardsCameraActivity.class);
# startActivityForResult(intent,REQUEST_SCANACTIVITY);
# # Receiving Preview Recognition Data
# if(requestCode==REQUEST_SCANACTIVITY&&resultCode==Activity.RESULT_OK){
# // After the recognition in the scanning interface is completed, the data is passed back
# if(data!=null){
# // Obtain the passed-back data
# Bundle bundle=data.getBundleExtra("resultbundle");
# // If the bundle is not null, it means the recognition is successful
# if(bundle!=null){
# CameraScanSucess(bundle);
# }else{
# String error=data.getStringExtra("error");
# CameraScanError(error);
# }
# }
# }
# private void CameraScanSucess(Bundle bundle) {
# ResultMessage resultMessage=(ResultMessage) bundle.
# getSerializable("resultMessage");
# String[] picPath=bundle.getStringArray("picpath");
# // Package the data
# String result=ManageIDCardRecogResult.managerSucessRecogResult(resultMessage);
# try {
# /** Passed results
# * @param recogResult Recognition result
# * @param picPath Array of image paths, picPath[0]: Full image path; picPath[2]: Cropped image; picPath[3]: ID photo
# */
# Intent intent = new Intent(this,ShowResultActivity.class);
# intent.putExtra("recogResult", result);
# intent.putExtra("fullPagePath",picPath[0]);
# intent.putExtra("cutPagePath",picPath[1]);
# startActivity(intent);
# }catch (Exception e){
# }
# }
# // Recognition Error
# private void CameraScanError(String error) {
# try {
# Intent intent = new Intent(this,ShowResultActivity.class);
# intent.putExtra("exception", error);
# startActivity(intent);
# }catch (Exception e){
# }
# }
# # Explanation of Main Document Types
# Document Name Document MAINID (in decimal)
# First-generation ID Card (Not currently supported) 1
# Front of Second-generation ID Card 2
# Back of Second-generation ID Card 3
# Temporary ID Card 4
# Driver's License 5
# Vehicle License 6
# Military Officer's ID 7
# Soldier's ID (Not currently supported) 8
# Machine-readable Documents Passport Page Mainland Travel Permit for Hong Kong and Macao Residents 9
# Taiwan Resident Travel Permit to the Mainland 10
# Mainland Resident Travel Permit to Taiwan 11
# Visa 12
# Passport 13
# Card Page Front of Mainland Travel Permit for Hong Kong and Macao Residents 14
# Back of Mainland Travel Permit for Hong Kong and Macao Residents 15
# Household Register 16
# New Version of Hong Kong and Macao Permit 22
# Exit-Entry Permit for Border Areas 23
# Front of New Version of Taiwan Resident Travel Permit to the Mainland 25
# Back of New Version of Taiwan Resident Travel Permit to the Mainland 26
# Supplementary Page of Motor Vehicle Driver's License 28
# Photo Page of 2017 Version of Travel Permit to Taiwan 29
# Supplementary Page of Motor Vehicle License 30
# Photo Page of Residence Permit for Hong Kong, Macao, and Taiwan Residents 31
# Issuing Authority Page of Residence Permit for Hong Kong, Macao, and Taiwan Residents 32
# Photo Page of 2017 Version of Foreigner's Permanent Residence ID Card 33
# Electronic Driver's License 34
# Electronic Vehicle License 35
# Supplementary Page of Electronic Driver's License 36
# Supplementary Page of Electronic Vehicle License 37
# Residence Permit 1000
# 2003 Version of Hong Kong Permanent Identity Card 1001(1)
# 2018 Version of Hong Kong Identity Card 1001(2)
# Boarding Pass (Currently not supported by the camera device) (Not currently supported) 1002
# Border Resident Permit (A) (Photo Page) (Not currently supported) 1003
# Border Resident Permit (B) (Personal Information Page) (Not currently supported) 1004
# Macao Identity Card 1005
# Collection Voucher (Supported by scanners) (Not currently supported) 1006
# Lawyer's License (A) (Information Page) 1007
# Lawyer's License (B) (Photo Page) 1008
# IC Card of Road Transport Certificate of the People's Republic of China (Not currently supported) 1009
# New Version of Macao Identity Card (Not currently supported) 1012
# New Version of Shenzhen Residence Permit 1013
# Macao Blue Card 1014
# Hong Kong Entry Slip 1015
# Machine-readable Code of Passport (Not currently supported) 1020
# Beijing Social Security Card 1021
# National Health Insurance Card 1030
# Front of Taiwan Identity Card 1031
# Back of Taiwan Identity Card 1032
# MRZ3*30 (Not currently supported) 1033
# MRZ2*44 (Not currently supported) 1034
# MRZ2*36 (Not currently supported) 1036
# Xiamen Social Security Card 1039
# Photo Page of Fujian Social Security Card 1041
# Malaysian Identity Card 2001
# California Driver's License 2002
# New Zealand Driver's License 2003
# Singapore Identity Card 2004
# ID2 (MRZ2*36) TD-2 Type Machine-readable Code 2006
# ID1 (MRZ3*30) (Not currently supported) 2007
# ID3 (MRZ2*44) (Not currently supported) 2008
# TD-1 Type Machine-readable Code 2009
# Indonesian Resident Identity Card - KEPT 2010(1)
# Indonesian Resident Identity Card - KPT 2010(2)
# Front of Thai Identity Card 2011
# Thai Driver's License 2012(1)
# Thai Driver's License - Private Car 2012(2)
# Mexican Voter ID AB 2013(1)
# Mexican Voter ID C 2013(2)
# Mexican Voter ID DE 2013(3)
# Back of Mexican Voter ID ABC 2014
# Back of Thai Identity Card 2016
# Swedish Driver's License 2020
# Malaysian Driver's License 2021
# Philippine Identity Card 2022
# Singapore Driver's License 2031
# Indonesian Driver's License 2041
# Japanese Driver's License 2051
# Photo Page of UAE Identity Card 2063
# Machine-readable Code Page of UAE Identity Card 2064
# Photo Page of Jordanian Identity Card 2065
# Photo Page of Egyptian Identity Card 2081
# Back of Egyptian Identity Card 2082
# Photo Page of Pakistani Identity Card 2083
# Photo Page of Saudi Identity Card 2085
# Photo Page of Saudi Residence Permit 2086
# Photo Page of Lebanese Identity Card 2087
# Machine-readable Code Page of UAE Identity Card 2092
# Photo Page of Djiboutian Identity Card 2093
# Photo Page of Kuwaiti Identity Card 2094
# Photo Page of Omani Identity Card 2095
# Photo Page of Omani Resident Card 2096
# Photo Page of Algerian Identity Card 2097
# Photo Page of Pakistani Identity Card 2098
# Saudi Identity Card 2110
# # NFC Function Integration
# 1.1 Import nfcLibrary.aar and add dependencies.
# 1.2 Add dependencies:
# implementation 'org.jmrtd:jmrtd:0.7.42'
# implementation 'edu.ucar:jj2000:5.2'
# implementation 'com.github.mhshams:jnbis:2.1.2'
# implementation 'net.sf.scuba:scuba-sc-android:0.0.26'
# 1.3 Add the following under the android tag in the main project:
# android {
# packagingOptions {
# exclude 'META-INF/versions/9/OSGI-INF/MANIFEST.MF'
# }
# }
# # Call Process
# 1.3 Implement the IChipReadResult interface.
# 1.4 Initialize: NfcRead.Companion.init(this, Devcode.devcode, this, false,true);
# Parameter 1: activity
# Parameter 2: Development code
# Parameter 3: Implementation class of the IChipReadResult interface
# Parameter 4: Whether to print logs
# Parameter 5: Whether to read the NFC profile picture
# Return value: int, see the authorization error code
# 1.5 Call NFC
# NfcRead.Companion.open(passportNumber, birthDate, expirationDate,mainId);
# Parameter 1: Passport number
# Parameter 2: Date of birth
# Parameter 3: Passport expiration date
# Parameter 4: Document type mainId (13, 14, 15, 22, 25, 26, 29)
# Return value: int:
# -10701: The device does not support NFC function.
# -10702: Initialization failed.
# -10703: The device has not enabled the NFC function.
# -10704: NFC is not authorized.
# For more details, refer to the demo.