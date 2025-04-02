# ID-documents-OCR-Android
# 调用预览识别
# 注：具体的代码调用流程可参看示例代码中的AllCardMainActivity.java中的方法
# 
# 相机界面的跳转
# CardOcrRecogConfigure.getInstance()
#                 //设置识别返回的语言
#                 .initLanguage(getApplicationContext())
#                 //是否保存裁切图
#                 .setSaveCut(true)
#                 //证件类型的ID
#                 .setnMainId(SharedPreferencesHelper.getInt(
#                         getApplicationContext(), "nMainId", 2))
#                 //证件类型的子ID
#                 .setnSubID( SharedPreferencesHelper.getInt(
#                         getApplicationContext(), "nSubID", 0))
#                 //身份证的正反面区分 0-自动区分；1-只识别正面；2-只识别反面（注：不设置默认为0）
#                 .setFlag(0)
#                 //设置扫描的方式 0-指导框扫描；1-实时监测边线
#                 .setnCropType(1)
#                 //设置图片的存储路径(注：默认路径为:Environment.getExternalStorageDirectory().toString()
#                 //            + "/wtimage/")
#                 .setSavePath(new DefaultPicSavePath());
#  Intent intent=new Intent(AllCardMainActivity.this,
# CardsCameraActivity.class);
#  startActivityForResult(intent,REQUEST_SCANACTIVITY);
# 
# 预览识别数据的接收
# if(requestCode==REQUEST_SCANACTIVITY&&resultCode==Activity.RESULT_OK){
#             //跳转扫描界面识别完成之后，数据回传
#             if(data!=null){
#                 //数据回传的获取
#                 Bundle bundle=data.getBundleExtra("resultbundle");
#                 //bundle不为null，代表着识别成功
#                 if(bundle!=null){
#                     CameraScanSucess(bundle);
#                 }else{
#                     String error=data.getStringExtra("error");
#                     CameraScanError(error);
#                 }
#             }
#   }
# private void CameraScanSucess(Bundle bundle) {
#         ResultMessage resultMessage=(ResultMessage) bundle.
# getSerializable("resultMessage");
#         String[] picPath=bundle.getStringArray("picpath");
#         //数据的封装
#         String result=ManageIDCardRecogResult.managerSucessRecogResult(resultMessage);
#         try {
#             /**传递的结果
#              * @param recogResult 识别结果
#              * @param picPath 图片路径数组，picPath[0]: 全图路径；picPath[2]: 裁切图；picPath[3]: 证件头像
#              */
#             Intent intent = new Intent(this,ShowResultActivity.class);
#             intent.putExtra("recogResult", result);
#             intent.putExtra("fullPagePath",picPath[0]);
#             intent.putExtra("cutPagePath",picPath[1]);
#             startActivity(intent);
#         }catch (Exception e){
#         }
#     }
# //识别错误
#    private void CameraScanError(String error) {
#         try {
#             Intent intent = new Intent(this,ShowResultActivity.class);
#             intent.putExtra("exception", error);
#             startActivity(intent);
#         }catch (Exception e){
#         }
#     }
# 
# 
# 证件主类型说明
# 证件名称	证件MAINID（10进制表示）
# 一代身份证（暂不支持）	1
# 二代身份证正面	2
# 二代身份证证背面	3
# 临时身份证	4
# 驾照	5
# 行驶证	6
# 军官证	7
# 士兵证（暂不支持）	8
# 机读证件	护照幅面	中华人民共和国往来港澳通行证	9
# 		台湾居民往来大陆通行证	10
# 		大陆居民往来台湾通行证	11
# 		签证	12
# 		护照	13
# 	卡幅面	港澳居民来往内地通行证正面	14
# 		港澳居民来往内地通行证背面	15
# 户口本	16
# 新版港澳通行证	22
# 边境地区出入境通行证	23
# 新版台湾居民往来大陆通行证（正面）	25
# 新版台湾居民往来大陆通行证（背面）	26
# 机动车驾驶证副页	28
# 往来台湾通行证2017版-照片页	29
# 机动车行驶证副页	30
# 港澳台居民居住证-照片页	31
# 港澳台居民居住证-签发机关页	32
# 外国人永久居留身份证2017版-照片页	33
# 电子驾照	34
# 电子行驶证	35
# 电子驾照副页	36
# 电子行驶证副业	37
# 居住证	1000
# 香港永久性居民身份证2003版本	1001(1)
# 香港居民身份证2018版	1001(2)
# 登机牌（拍照设备目前不支持登机牌的识别）（暂不支持）	1002
# 边民证（A）（照片页）（暂不支持）	1003
# 边民证（B）（个人信息页）（暂不支持）	1004
# 澳门身份证	1005
# 领取凭证(扫描仪支持) （暂不支持）	1006
# 律师证（A）（信息页）	1007
# 律师证（B） （照片页）	1008
# 中华人民共和国道路运输证IC卡（暂不支持）	1009
# 新版澳门身份证（暂不支持）	1012
# 新版深圳市居住证	1013
# 澳门蓝卡	1014
# 香港入境小票	1015
# 护照机读码（暂不支持）	1020
# 北京社保卡	1021
# 全民健康保险卡	1030
# 台湾身份证正面	1031
# 台湾身份证背面	1032
# MRZ3*30（暂不支持）	1033
# MRZ2*44（暂不支持）	1034
# MRZ2*36（暂不支持）	1036
# 厦门社会保障卡	1039
# 福建社会保障卡-照片页	1041
# 马来西亚身份证	2001
# 加利福尼亚驾照	2002
# 新西兰驾照	2003
# 新加坡身份证	2004
# ID2（MRZ2*36）TD-2型机读码	2006
# ID1（MRZ3*30）（暂不支持）	2007
# ID3（MRZ2*44）（暂不支持）	2008
# TD-1型机读码	2009
# 印度尼西亚居民身份证-KEPT	2010(1)
# 印度尼西亚居民身份证-KPT	2010(2)
# 泰国身份证（正面）	2011
# 泰国驾驶证	2012(1)
# 泰国驾驶证-私家车	2012(2)
# 墨西哥选民证AB	2013(1)
# 墨西哥选民证C	2013(2)
# 墨西哥选民证DE	2013(3)
# 墨西哥选民证背面ABC	2014
# 泰国身份证（反面）	2016
# 瑞典驾驶证	2020
# 马来西亚驾照	2021
# 菲律宾身份证	2022
# 新加坡驾驶证	2031
# 印度尼西亚驾驶证	2041
# 日本驾照	2051
# 阿联酋身份证-照片页	2063
# 阿联酋身份证-机读码页	2064
# 约旦身份证-照片页	2065
# 埃及身份证-照片页	2081
# 埃及身份证-背面	2082
# 巴基斯坦身份证-照片页	2083
# 沙特身份证-照片页	2085
# 沙特居留证-照片页	2086
# 黎巴嫩身份证-照片页	2087
# 阿联酋身份证-机读码页	2092
# 吉布提身份证-照片页	2093
# 科威特身份证-照片页	2094
# 阿曼身份证-照片页	2095
# 阿曼居民证-照片页	2096
# 阿尔及利亚身份证-照片页	2097
# 巴基斯坦身份证-照片页	2098
# 沙特身份证	2110
# 
# 
# nfc功能集成
# 1.1	导入nfcLibrary.aar 并添加依赖
# 1.2	添加依赖：
# implementation 'org.jmrtd:jmrtd:0.7.42'
# implementation 'edu.ucar:jj2000:5.2'
# implementation 'com.github.mhshams:jnbis:2.1.2'
# implementation  'net.sf.scuba:scuba-sc-android:0.0.26'
# 1.3 主公程android 标签下添加
# android {
# packagingOptions {
#         exclude 'META-INF/versions/9/OSGI-INF/MANIFEST.MF'
# 
#     }
# }
# 
# 调用流程
# 1.3	实现 IChipReadResult接口
# 1.4	初始化：NfcRead.Companion.init(this, Devcode.devcode, this, false,true);
# 参数1：activity
# 参数2：开发码
# 参数3：IChipReadResult接口实现类
# 参数4：是否打印日志
# 参数5：是否读取nfc头像图片
# 返回值：int 见授权错误码
# 1.5	调用nfc
# NfcRead.Companion.open(passportNumber, birthDate, expirationDate,mainId);
# 参数1：护照号码
# 参数2：出生日期
# 参数3：护照有效期
# 参数4：证件类型mainId(13，14，15，22，25，26，29)
# 返回值：int ：
# -10701 :设备不支持NFC功能
# -10702 :初始化失败
# -10703 :设备未开启NFC功能
# -10704：没有授权nfc
# 
# 
# 
# 详细可参考demo
# 