/**
 *
 */
package com.kernal.passportreader.sdk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kernal.passportreader.sdk.adapter.CountryData;
import com.kernal.passportreader.sdk.adapter.ResultAdapter;
import com.kernal.passportreader.sdk.adapter.ResultData;
import com.kernal.passportreader.sdk.nfc.IChipReadResult;
import com.kernal.passportreader.sdk.nfc.NfcInfo;
import com.kernal.passportreader.sdk.nfc.NfcRead;
import com.kernal.passportreader.sdk.utils.Devcode;
import com.kernal.passportreader.sdk.utils.NetworkProber;
import com.kernal.passportreader.sdk.view.HttpUploadDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Project Name: PassportReader_Sample_Sdk Category name:ShowResultActivity
 * Category description  Creator: yujin  Creating time: 12th, June 2015.
 * Reviser:huangzhen Revising time: 12th, June 2015. Modifying remarks.
 */
public class ShowResultActivity extends Activity implements OnClickListener, IChipReadResult, ResultAdapter.UpDataCallback {
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private ImageView iv_recogPicture;
    private String recogResult = "";
    private String exception;
    private String[] splite_Result;
    private String result = "";
    private Button btn_ok;
    private ImageView btn_nfc, scanAvatar, chipAvatar;

    private ConstraintLayout dg_group;
    private TextView btn_back;
    private TextView mrzText, mrz1, mrz2, mrz3, recogError;
    private String devcode = "";
    public static String cutPagePath = "";
    public static String HeadJpgPath = "";
    public static String fullPagePath = "";
    private boolean isSaveFullPic = false;
    private TextView tv_set;
    private boolean importRecog = false;
    private int mainId = 2;

    private RecyclerView mRecycleView;
    private ResultAdapter mResultAdapter;
    private List<ResultData> mResultDatas = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<String> mrzs = new ArrayList<String>();
    private ArrayList<String> vizs = new ArrayList<String>();
    private ConcurrentHashMap<String, CountryData> countryMap = new ConcurrentHashMap<>();

    private String passportNumber, birthDate, expirationDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//  hiding titles
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //		WindowManager.LayoutParams.FLAG_FULLSCREEN);//setting up the full screen
        //  Screen always on
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setContentView(com.kernal.passportreader.myapplication.R.layout.activity_show_result_new);
        Intent intent = getIntent();
        recogResult = intent.getStringExtra("recogResult");
        exception = intent.getStringExtra("exception");
        devcode = intent.getStringExtra("devcode");
        cutPagePath = intent.getStringExtra("cutPagePath");
        HeadJpgPath = intent.getStringExtra("HeadJpgPath");
        fullPagePath = intent.getStringExtra("fullPagePath");
        importRecog = intent.getBooleanExtra("importRecog", false);
        mainId = intent.getIntExtra("mainId", 2);
        findView();
        //  the came interface being released

        NfcRead.Companion.init(this, Devcode.devcode, this, false, true);
    }

    /**
     * @return void 返回类型
     * @throws
     * @Title: findView
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private void findView() {
        // TODO Auto-generated method stub
        iv_recogPicture = (ImageView) findViewById(getResources().getIdentifier(
                "iv_recogPicture", "id", getPackageName()));
        btn_back = (TextView) findViewById(getResources()
                .getIdentifier("btn_back", "id", getPackageName()));
        btn_back.setOnClickListener(this);
        btn_ok = (Button) findViewById(getResources()
                .getIdentifier("btn_ok", "id", getPackageName()));
        btn_ok.setOnClickListener(this);
        btn_nfc = (ImageView) findViewById(getResources()
                .getIdentifier("btn_nfc", "id", getPackageName()));
        btn_nfc.setOnClickListener(this);
        tv_set = (TextView) findViewById(getResources()
                .getIdentifier("tv_set", "id", getPackageName()));
        tv_set.setOnClickListener(this);
        mRecycleView = findViewById(com.kernal.passportreader.myapplication.R.id.recycle_view);
        mrzText = findViewById(com.kernal.passportreader.myapplication.R.id.mrzText);
        mrz1 = findViewById(com.kernal.passportreader.myapplication.R.id.mrz1);
        mrz3 = findViewById(com.kernal.passportreader.myapplication.R.id.mrz3);
        recogError = findViewById(com.kernal.passportreader.myapplication.R.id.recogError);
        mrz2 = findViewById(com.kernal.passportreader.myapplication.R.id.mrz2);
        scanAvatar = findViewById(com.kernal.passportreader.myapplication.R.id.scanAvatar);
        chipAvatar = findViewById(com.kernal.passportreader.myapplication.R.id.chipAvatar);
        dg_group = findViewById(com.kernal.passportreader.myapplication.R.id.dg_group);


//        DividerItemDecoration mDivider = new
//                DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
//        mRecycleView.addItemDecoration(mDivider);
        if (cutPagePath != null && !cutPagePath.equals("")) {
            iv_recogPicture.setImageBitmap(BitmapFactory.decodeFile(cutPagePath));
        } else if (fullPagePath != null && !fullPagePath.equals("")) {
            iv_recogPicture.setImageBitmap(BitmapFactory.decodeFile(fullPagePath));
        }

        if (mainId == 13 || mainId == 14 || mainId == 15 || mainId == 22 || mainId == 25 || mainId == 26 || mainId == 29) {
            //如果是护照的话，显示nfc按钮
            btn_nfc.setVisibility(View.VISIBLE);
            btn_ok.setVisibility(View.GONE);
            mrzText.setVisibility(View.VISIBLE);
            mrz1.setVisibility(View.VISIBLE);
            mrz2.setVisibility(View.VISIBLE);
            mrz3.setVisibility(View.VISIBLE);
            scanAvatar.setVisibility(View.VISIBLE);
            chipAvatar.setVisibility(View.VISIBLE);
            dg_group.setVisibility(View.VISIBLE);
        }


        String[] countrySplit;
        try {
            InputStream inputStream = getAssets().open("idcardocr/IDCDF/country_names.dat");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_16LE));
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println("国家数据："+line);
                countrySplit = line.split("\\|");
                if (countrySplit.length == 3) {
                    CountryData countryData = new CountryData();
                    countryData.code = countrySplit[0];
                    countryData.valueEnglish = countrySplit[1];
                    countryData.valueChinese = countrySplit[2];
                    countryMap.put(countryData.code, countryData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //mrz
        mrzs.add(getString(R.string.Sex));
        mrzs.add(getString(R.string.Issuing_country_code));
        mrzs.add(getString(R.string.Nationality_code));
        mrzs.add(getString(R.string.English_name));
        mrzs.add(getString(R.string.passport_number));
        mrzs.add(getString(R.string.Date_of_birth));
        mrzs.add(getString(R.string.Date_of_expiry));
        if (mainId == 14 || mainId == 15 || mainId == 22 || mainId == 25 || mainId == 26 || mainId == 29) {
            mrzs.add(getString(R.string.id_number));
            mrzs.add(getString(R.string.China_name));
            mrzs.add(getString(R.string.renewal_code));
            mrzs.add(getString(R.string.Issuing_times));
            mrzs.add(getString(R.string.Issuing_authority));
        }
        if (recogResult != null) {
            //viz
            //只有中国人才显示本国姓名
            if (recogResult.contains(getString(R.string.Nationality_code) + ":CHN"))
                vizs.add(getString(R.string.National_name));
            vizs.add(getString(R.string.Place_of_issue_ocr));
            vizs.add(getString(R.string.Place_of_birth_ocr));
            vizs.add(getString(R.string.Date_of_issue_ocr));
            vizs.add(getString(R.string.Issuing_authority_ocr));

            System.out.println("recogResult:"+recogResult);
            splite_Result = recogResult.split("#");
            String mrzStr = "";
            for (int i = 0; i < splite_Result.length; i++) {
                if (result.equals("")) {
                    result = splite_Result[i] + "\n";
                } else {
                    result = result + splite_Result[i] + "\n";
                }

                String[] split = splite_Result[i].split(":");
                if (split.length > 1) {
                    ResultData resultData = new ResultData();

                    if (split[0].equals(getString(R.string.passport_number_MRZ))) {
                        resultData.setKey(getString(R.string.passport_number));
                    } else if (split[0].equals(getString(R.string.passport_number))) {

                    } else {
                        resultData.setKey(split[0]);
                    }
                    resultData.setValue(split[1]);
                    if (mainId == 13) {
                        formatResultData(resultData);
                    } else if (mainId == 14 || mainId == 15 || mainId == 22 || mainId == 25 || mainId == 26 || mainId == 29) {
                        if (resultData.getKey().equals("MRZ1") || resultData.getKey().equals("MRZ2") || resultData.getKey().equals("MRZ3")) {
                            mrzStr = mrzStr + resultData.getValue() + "\n";
                        }
//                        resultData.setIconType(3);
//                        mResultDatas.add(resultData);

                        formatResultData(resultData);
                    } else {
                        mResultDatas.add(resultData);
                    }
                }
            }
            if (mainId == 14 || mainId == 15 || mainId == 22 || mainId == 25 || mainId == 26 || mainId == 29) {
                mrz3.setText(mrzStr);
            }
        } else {
            recogError.setVisibility(View.VISIBLE);
            recogError.setText(getString(R.string.exception9));
        }
        if (HeadJpgPath != null && !HeadJpgPath.isEmpty())
            scanAvatar.setImageURI(Uri.fromFile(new File(HeadJpgPath)));
        mResultAdapter = new ResultAdapter(this, this);
        mResultAdapter.setResultDatas(mResultDatas);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleView.setAdapter(mResultAdapter);


    }

    private void formatResultData(ResultData resultData) {
        if (mrzs.contains(resultData.getKey())) {
            if (mainId == 14 || mainId == 15 || mainId == 22 || mainId == 25 || mainId == 26 || mainId == 29) {
                resultData.setIconType(3);
            } else {
                resultData.setIconType(2);
            }
            if (resultData.getKey().equals(getString(R.string.Issuing_country_code)) ||
                    resultData.getKey().equals(getString(R.string.Nationality_code))) {
                CountryData countryData = countryMap.get(resultData.getValue());
                if (countryData != null && !countryData.valueChinese.isEmpty()) {
                    if (getResources().getConfiguration().locale.getLanguage().equals("zh"))
                        resultData.setValue(countryData.valueChinese + "(" + resultData.getValue() + ")");
                    else
                        resultData.setValue(countryData.valueEnglish + "(" + resultData.getValue() + ")");
                }

            }
            mResultDatas.add(resultData);
        } else if (vizs.contains(resultData.getKey())) {
            resultData.setIconType(3);
            if (resultData.getKey().equals(getString(R.string.Issuing_authority_ocr))) {
                resultData.setKey(getString(R.string.Issuing_authority));
            }
            if (resultData.getKey().equals(getString(R.string.Place_of_birth_ocr))) {
                resultData.setKey(getString(R.string.Place_of_birth));
            }
            if (resultData.getKey().equals(getString(R.string.Place_of_issue_ocr))) {
                resultData.setKey(getString(R.string.Place_of_issue));
            }
            if (resultData.getKey().equals(getString(R.string.Date_of_issue_ocr))) {
                resultData.setKey(getString(R.string.Date_of_issue));
            }
            mResultDatas.add(resultData);
        } else if (mainId == 13) {
            if (resultData.getKey().equals("MRZ1")) {
                mrz1.setText(resultData.getValue());
            } else if (resultData.getKey().equals("MRZ2")) {
                mrz2.setText(resultData.getValue());
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        if (getResources().getIdentifier("btn_back", "id",
                this.getPackageName()) == v.getId()) {
            if (importRecog) {
                ShowResultActivity.this.finish();
            } else {
                if (!isSaveFullPic) {
                    if (fullPagePath != null && !fullPagePath.equals("")) {
                        File file = new File(fullPagePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
             /*   intent = new Intent(this,CardsCameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                ShowResultActivity.this.finish();
            }
        } else if (getResources().getIdentifier("btn_nfc", "id",
                this.getPackageName()) == v.getId()) {

            //System.out.println("nfc功能");

            for (ResultData resultData : mResultDatas) {
                if (mainId == 13) {
                    if (resultData.getKey().equals(getString(R.string.passport_number))) {
                        passportNumber = resultData.getValue();
                    } else if (resultData.getKey().equals(getString(R.string.Date_of_expiry))) {
                        expirationDate = resultData.getValue();
                    }
                } else if (mainId == 14 || mainId == 15 || mainId == 22 || mainId == 25 || mainId == 26 || mainId == 29) {
                    if (resultData.getKey().equals(getString(R.string.id_number))) {
                        passportNumber = resultData.getValue();
                    } else if (resultData.getKey().equals(getString(R.string.Date_of_expiry))) {
                        expirationDate = resultData.getValue();
                    }
                }
                if (resultData.getKey().equals(getString(R.string.Date_of_birth))) {
                    birthDate = resultData.getValue();
                }
            }
            if (passportNumber == null || birthDate == null || expirationDate == null) {
                Toast.makeText(this, getString(R.string.incomplete_information), Toast.LENGTH_SHORT).show();
                return;
            }
            birthDate = birthDate.replace("-", "");
            if (expirationDate.length() > 10) {
                String[] strings = expirationDate.split("-");
                expirationDate = strings[1];
            } else {
                expirationDate = expirationDate.replace("-", "");
            }
            if (passportNumber.isEmpty() || birthDate.length() != 8 || expirationDate.length() != 8) {
                Toast.makeText(this, getString(R.string.incomplete_information), Toast.LENGTH_SHORT).show();
                return;
            }


            birthDate = birthDate.substring(2);
            expirationDate = expirationDate.substring(2);
            String pattern = "^\\d{6}$";

            Pattern r = Pattern.compile(pattern);
            Matcher isEffectiveBirthDate = r.matcher(birthDate);
            Matcher isEffectiveExpirationDate = r.matcher(expirationDate);

            System.out.println(isEffectiveBirthDate.matches());
            System.out.println(isEffectiveExpirationDate.matches());


            System.out.println("passportNumber:" + passportNumber);
            System.out.println("birthDate:" + birthDate);
            System.out.println("expirationDate:" + expirationDate);

            if (isEffectiveBirthDate.matches() && isEffectiveExpirationDate.matches()) {

                int open = NfcRead.Companion.open(passportNumber, birthDate, expirationDate, mainId);
                System.out.println("open:" + open);
                if (open == -10701) {
                    Toast.makeText(this, open + getString(R.string.doest_support_NFC), Toast.LENGTH_SHORT).show();
                } else if (open == -10702) {
                    Toast.makeText(this, open + getString(R.string.initialization_failure), Toast.LENGTH_SHORT).show();
                } else if (open == -10703) {
                    Toast.makeText(this, open + getString(R.string.NFC_is_disabled), Toast.LENGTH_SHORT).show();
                } else if (open == -10704) {
                    Toast.makeText(this, open + getString(R.string.NFC_is_authorized), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.Date_format_error), Toast.LENGTH_SHORT).show();
            }
        } else if (getResources().getIdentifier("btn_ok", "id",
                this.getPackageName()) == v.getId()) {
            if (importRecog) {
                if (cutPagePath != null && !cutPagePath.equals("")) {
                    File file = new File(cutPagePath);
                    if (file.exists()) {
                        file.delete();
                        sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.parse("file://" + cutPagePath)));
                    }
                }
            } else {
                if (!isSaveFullPic) {
                    if (fullPagePath != null && !fullPagePath.equals("")) {
                        File file = new File(fullPagePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }
        /*    if(VehicleLicenseflag==0){
                intent = new Intent(ShowResultActivity.this,AllCardMainActivity.class);
            }else if(VehicleLicenseflag==1){
                intent = new Intent(ShowResultActivity.this,IdcardMainActivity.class);
            }else if(VehicleLicenseflag==2){
                intent = new Intent(ShowResultActivity.this,VehicleLicenseMainActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            ShowResultActivity.this.finish();
        } else if (getResources().getIdentifier("tv_set", "id",
                this.getPackageName()) == v.getId()) {
            if (NetworkProber.isNetworkAvailable(ShowResultActivity.this)) {
                File file = null;
                if (fullPagePath != null && !fullPagePath.equals("")) {
                    file = new File(ShowResultActivity.fullPagePath);
                }
                if (file != null && file.exists() && !file.isDirectory()) {
                    HttpUploadDialog myHttpUploadDialog = new HttpUploadDialog();
                    myHttpUploadDialog.show(getFragmentManager(), "HttpUploadDialog");

                } else {
                    Toast.makeText(ShowResultActivity.this, getString(getResources().getIdentifier("filenoexists", "string",
                            getPackageName())), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ShowResultActivity.this, getString(getResources().getIdentifier("network_unused", "string",
                        getPackageName())), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (importRecog) {
                ShowResultActivity.this.finish();
            } else {
                if (!isSaveFullPic) {
                    if (fullPagePath != null && !fullPagePath.equals("")) {
                        File file = new File(fullPagePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            /*    Intent  intent = new Intent(this,CardsCameraActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                ShowResultActivity.this.finish();
            }
        }
        return true;
    }

    @Override
    public void resultData(String data, Bitmap bitmap, NfcInfo nfcInfo, LinkedHashMap<String, Boolean> dgMap) {

        runOnUiThread(() -> {
            //dg2
            chipAvatar.setImageBitmap(bitmap);

            for (int i = 0; i < mResultDatas.size(); i++) {

                ResultData resultData = mResultDatas.get(i);

                //性别
                if (Objects.equals(resultData.getKey(), getString(R.string.Sex))) {
                    resultData.setValue(nfcInfo.getSex().startsWith("F") ? "女/F" : "男/M");
                    resultData.setIconType(4);
                    mResultDatas.set(i, resultData);
                } else if (Objects.equals(resultData.getKey(), getString(R.string.Issuing_country_code))) {
                    //签发国代码
                    CountryData countryData = countryMap.get(nfcInfo.getIssuing_country_code());
                    if (countryData != null && !countryData.valueChinese.isEmpty()) {
                        if (getResources().getConfiguration().locale.getLanguage().equals("zh"))
                            resultData.setValue(countryData.valueChinese + "(" + nfcInfo.getIssuing_country_code() + ")");
                        else
                            resultData.setValue(countryData.valueEnglish + "(" + nfcInfo.getIssuing_country_code() + ")");
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.Nationality_code))) {
                    //持证人国籍代码
                    CountryData countryData = countryMap.get(nfcInfo.getNationality_code());
                    if (countryData != null && !countryData.valueChinese.isEmpty()) {
                        if (getResources().getConfiguration().locale.getLanguage().equals("zh"))
                            resultData.setValue(countryData.valueChinese + "(" + nfcInfo.getNationality_code() + ")");
                        else
                            resultData.setValue(countryData.valueEnglish + "(" + nfcInfo.getNationality_code() + ")");
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.English_name))) {
                    //英文姓名
                    if (!nfcInfo.getEnglish_name().isEmpty()) {
                        resultData.setValue(nfcInfo.getEnglish_name());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.id_number))) {
                    //证件号码
                    if (!nfcInfo.getPassport_number().isEmpty()) {
                        resultData.setValue(nfcInfo.getPassport_number());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.passport_number))) {
                    //护照号码
                    if (!nfcInfo.getPassport_number().isEmpty()) {
                        resultData.setValue(nfcInfo.getPassport_number());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.Date_of_birth))) {
                    //出生日期
                    if (!nfcInfo.getDate_of_birth().isEmpty()) {
                        resultData.setValue(dataFormat(nfcInfo.getDate_of_birth()));
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.Date_of_expiry))) {
                    //有效期至
                    if (!nfcInfo.getDate_of_expiry().isEmpty()) {
                        resultData.setValue(dataFormat(nfcInfo.getDate_of_expiry()));
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.China_name))) {
                    //中文姓名
                    if (!nfcInfo.getNational_name().isEmpty()) {
                        resultData.setValue(nfcInfo.getNational_name());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.National_name))) {
                    //本国姓名
                    if (!nfcInfo.getNational_name().isEmpty()) {
                        resultData.setValue(nfcInfo.getNational_name());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.Place_of_birth))) {
                    //出生地点
                    if (!nfcInfo.getPlace_of_birth().isEmpty()) {
                        resultData.setValue(nfcInfo.getPlace_of_birth());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.Place_of_issue))) {
                    //签发地点
                    if (!nfcInfo.getPlace_of_issue().isEmpty()) {
                        resultData.setValue(nfcInfo.getPlace_of_issue());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }

                } else if (Objects.equals(resultData.getKey(), getString(R.string.Date_of_issue))) {
                    //签发日期
                    if (!nfcInfo.getDate_of_issue().isEmpty()) {
                        resultData.setValue(nfcInfo.getDate_of_issue());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }

                } else if (Objects.equals(resultData.getKey(), getString(R.string.Issuing_authority))) {
                    //签发机关
                    if (!nfcInfo.getIssuing_authority().isEmpty()) {
                        resultData.setValue(nfcInfo.getIssuing_authority());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.renewal_code))) {
                    //换证次数
                    if (!nfcInfo.getRenewal_code().isEmpty()) {
                        resultData.setValue(nfcInfo.getRenewal_code());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                } else if (Objects.equals(resultData.getKey(), getString(R.string.Issuing_times))) {
                    //签发次数
                    if (!nfcInfo.getRenewal_code().isEmpty()) {
                        resultData.setValue(nfcInfo.getRenewal_code());
                        resultData.setIconType(4);
                        mResultDatas.set(i, resultData);
                    }
                }

                mResultAdapter.notifyItemChanged(i);
            }
            if (mainId == 14 || mainId == 15 || mainId == 22 || mainId == 25 || mainId == 26 || mainId == 29) {
                mrz3.setText(nfcInfo.getMrz());
            }
            //TODO　显示存在的文件
            int lineNumber = 9;
            int textWidth = dg_group.getWidth() / lineNumber;
            int textHeight = dg_group.getHeight() / 2;

            int index = 0;
            for (String key : dgMap.keySet()) {
                //System.out.println(key + ":" + dgMap.get(key));
                TextView textView = new TextView(this);
                textView.setTextSize(12);
                textView.setGravity(Gravity.CENTER);
                textView.setText(key);
                textView.setTextColor(Boolean.TRUE.equals(dgMap.get(key)) ? Color.parseColor("#00FF7F") : Color.parseColor("#AAAAAA"));
                textView.setId(View.generateViewId());
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(textWidth, textHeight);
                if (index == 0) {
                    layoutParams.leftToLeft = dg_group.getId();
                    layoutParams.topToTop = dg_group.getId();
                } else if (index == 9) {
                    layoutParams.leftToLeft = dg_group.getId();
                    layoutParams.topToBottom = dg_group.getChildAt(index - 1).getId();
                } else {
                    layoutParams.leftToRight = dg_group.getChildAt(index - 1).getId();
                    layoutParams.topToTop = dg_group.getChildAt(index - 1).getId();
                }
                // 设置TextView的宽度和高度
                textView.setLayoutParams(layoutParams);


                dg_group.addView(textView, layoutParams);
                // 添加约束条件，例如设置在ConstraintLayout的左上角

                index++;
                //dg_group


            }

        });
    }


    private String dataFormat(String data) {
        System.out.println("dataFormat:" + data);
        String[] split = data.split("\\.");

        if (split.length == 3)
            return split[2] + "-" + split[1] + "-" + split[0];
        else
            return data;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (nfcUtil != null) {
//            nfcUtil.close();
//        }
        NfcRead.Companion.close();
    }

    @Override
    public void updata(int position, ResultData resultData) {
        mResultDatas.set(position, resultData);
    }
}
