package widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.ljy.myconstraintdemo.R;
import com.mrwujay.cascade.addressModel.CityModel;
import com.mrwujay.cascade.addressModel.DistrictModel;
import com.mrwujay.cascade.addressModel.ProvinceModel;
import com.mrwujay.cascade.addressService.XmlAddressParserHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import widget.adapters.ArrayWheelAdapter;

/**
 * @author ZhaoFa_Lin on 2016/9/21 15:11
 * @Description 全国省市区，三级联动
 * @company 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class AddressPickerDatas implements OnWheelChangedListener {

    private static final String TAG = "AddressPickerData";
    /**
     * 所有省
     */
    protected String[] mProvinceDatasMap;
    protected HashMap<String, String> mProvinceAllMap = new HashMap<String, String>();
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCityDatasMap = new HashMap<String, String[]>();
    protected HashMap<String, HashMap<String, String>> mCityAllMap = new HashMap<String, HashMap<String, String>>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected HashMap<String, HashMap<String, String>> mDistrictAllMap = new HashMap<String, HashMap<String, String>>();
    /**
     * key - 区 values - 区域编码
     */
    protected Map<String, String> mDistrictCodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProvinceName;
    protected String mCurrentProvinceCode;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    protected String mCurrentCityCode;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName;
    protected String mCurrentDistrictCode;

    private String[] mCitys, mAreas;// 定义城市、区域数组
    private HashMap<String, String> mCityMaps, mAreaMaps;// 城市、区域map

    private Context context;
    private Dialog pickerDialog;
    private View addressView;
    private Button mBtnConfirm;// 确定
    private Button mBtnCancle;// 取消
    private WheelView mViewProvince;// 省
    private WheelView mViewCity;// 市
    private WheelView mViewDistrict;// 区
//    private TextView address;// 地址显示

    private AddressSelectedListener addressSelectedListener;

    /**
     * 区域选择Dialog
     */
    public void selectAddressDialog(Context context) {
        this.context = context;
        pickerDialog = new Dialog(context, R.style.MyDialogStyle);
        pickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        addressView = inflater.inflate(R.layout.picker_address, null);

        Window window = pickerDialog.getWindow();
        // 重新设置
        WindowManager.LayoutParams winMagParams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.addresspickerstyle); // 添加动画
        window.setAttributes(winMagParams);
        pickerDialog.setContentView(addressView);
        /*
		 * 将对话框的大小按屏幕大小的百分比设置
		 */
        WindowManager winMag = window.getWindowManager();
        Display display = winMag.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams params = window.getAttributes(); // 获取对话框当前的参数值
        //params.height = (int) (display.getHeight() * 0.4); // 高度设置为屏幕的0.6
        params.width = (int) (display.getWidth() * 1.0); // 宽度设置为屏幕的0.65
        window.setAttributes(params);
        winMagParams.x = 0; // 新位置X坐标
        winMagParams.y = 0; // 新位置Y坐标
        setUpViews();
        setUpListener();
        setUpData();
        pickerDialog.show();
    }

    /**
     * 初始化WheelView
     */
    private void setUpViews() {
        mViewProvince = (WheelView) addressView.findViewById(R.id.id_province);
        mViewCity = (WheelView) addressView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) addressView.findViewById(R.id.id_district);
        mBtnConfirm = (Button) addressView.findViewById(R.id.btn_confirm);
        mBtnCancle = (Button) addressView.findViewById(R.id.btn_cancle);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        try{
        if (wheel == mViewProvince) {
            upDataCitys();
        } else if (wheel == mViewCity) {
            upDataDistrict();
        } else if (wheel == mViewDistrict) {
            if (mDistrictDatasMap != null && mDistrictDatasMap.get(mCurrentCityCode).length != 0) {
                mCurrentDistrictCode = mDistrictDatasMap.get(mCurrentCityCode)[newValue];
                mCurrentDistrictName = mDistrictCodeDatasMap.get(mCurrentDistrictCode);
            } else {
                mCurrentDistrictCode = "";
                mCurrentDistrictName = "";
            }
        }
        }catch (Exception e){
            Log.e("datepickerException",e.getMessage());
        }
    }

    /**
     * 添加事件
     */
    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 显示数据
                showSelectedResult();
            }
        });
        mBtnCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 取消
                pickerDialog.dismiss();
                pickerDialog = null;
            }
        });
    }

    /**
     * 展示数据
     */
    private void showSelectedResult() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PROV_NAME", mCurrentProvinceName);
            jsonObject.put("CITY_NAME", mCurrentCityName);
            jsonObject.put("TOWN_NAME", mCurrentDistrictName);
            jsonObject.put("PROV_CODE", mCurrentProvinceCode);
            jsonObject.put("CITY_CODE", mCurrentCityCode);
            jsonObject.put("TOWN_CODE", mCurrentDistrictCode);
        } catch (JSONException e) {
            Log.e("JSONException",e.getMessage());
        }
            addressSelectedListener.onAddressSelectedListener(jsonObject.toString(), mCurrentProvinceCode, mCurrentCityCode, mCurrentDistrictCode);
        pickerDialog.dismiss();
        pickerDialog = null;
    }

    /**
     * 初始化数据
     */
    private void setUpData() {
        //initProvinceDatas();
        initData();
        String[] provinceDatas;
        ArrayList<String> provinceLists = new ArrayList<String>();
        for (String provinceCode : mProvinceDatasMap) {
            String provinceName = mProvinceAllMap.get(provinceCode);
            provinceLists.add(provinceName);
//            Logs.i(TAG, "Code-省=="+provinceName);
        }
        provinceDatas = new String[provinceLists.size()];
        provinceLists.toArray(provinceDatas);
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
                provinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        upDataCitys();
        upDataDistrict();
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void upDataCitys() {
        String[] cityDatas;
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProvinceCode = mProvinceDatasMap[pCurrent];
        mCitys = mCityDatasMap.get(mProvinceDatasMap[pCurrent]);
        mCityMaps = mCityAllMap.get(mProvinceDatasMap[pCurrent]);
        String[] citiesCode = mCityDatasMap.get(mCurrentProvinceCode);
        ArrayList<String> cityLists = new ArrayList<String>();
        for (String cityCode : citiesCode) {
            String cityName = mCityMaps.get(cityCode);
            cityLists.add(cityName);
//            Log.i(TAG, "Code-市=="+cityName);
        }
        cityDatas = new String[cityLists.size()];
        cityLists.toArray(cityDatas);
        if (cityDatas == null) {
            cityDatas = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cityDatas));
        mViewCity.setCurrentItem(0);
        mCurrentProvinceName = mProvinceAllMap.get(mProvinceDatasMap[pCurrent]);
//        Log.i("省份id", mProvinceDatasMap[pCurrent] + "==============="
//                + mCurrentProvinceName);
        upDataDistrict();
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void upDataDistrict() {
        int pCurrent = mViewCity.getCurrentItem();
        int disCurrent = mViewDistrict.getCurrentItem();
        String[] areasCodes = null;
        String[] districtDatas = null;
        if (isCityDataEmpty()) {
            mCurrentCityCode = mCityDatasMap.get(mCurrentProvinceCode)[pCurrent];
//            Log.i(TAG, "当前市的名称-Code=" + mCurrentCityCode);
            areasCodes = mDistrictDatasMap.get(mCurrentCityCode);
            if (areasCodes != null && areasCodes.length != 0 && disCurrent == 0) {
                mCurrentDistrictCode = areasCodes[disCurrent];
                mCurrentDistrictName = mDistrictCodeDatasMap.get(mCurrentDistrictCode);
            } else {
                mCurrentDistrictCode = "";
                mCurrentDistrictName = "";
            }
            if (isDistrictDataEmpty(mDistrictDatasMap, pCurrent)) {
                mAreas = mDistrictDatasMap.get(mCurrentCityCode);
                mAreaMaps = mDistrictAllMap.get(mCitys[pCurrent]);
                ArrayList<String> districtLists = new ArrayList<String>();
                for (String areasCode : areasCodes) {
                    String districtName = mAreaMaps.get(areasCode);
                    districtLists.add(districtName);
//                    Log.i(TAG, "Code-区/县=="+districtName);
                }
                districtDatas = new String[districtLists.size()];
                districtLists.toArray(districtDatas);
            } else {
                mCurrentDistrictCode = "";
                mCurrentDistrictName = "";
            }
        } else {
            mCurrentCityCode = "";
            mCurrentDistrictCode = "";
            mCurrentDistrictName = "";
        }
        if (districtDatas == null) {
            districtDatas = new String[]{""};
        }

        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context,
                districtDatas));
        mViewDistrict.setCurrentItem(0);
        // mCityText.setText(mCitys[pCurrent]);
        // 获取选择的城市id
        if (isCityDataEmpty()) {
            mCurrentCityName = mCityMaps.get(mCitys[pCurrent]);
//            Log.e("城市id", mCitys[pCurrent] + "============" + mCurrentCityName);
        } else {
            mCurrentCityName = "";
        }
    }

    /**
     * 解析省市区的XML数据
     */
    private void initData() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        InputStream input = null;
        try {
            input = asset.open("province_address_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser;
            parser = spf.newSAXParser();
            XmlAddressParserHandler handler = new XmlAddressParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            // 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProvinceName = provinceList.get(0).getName();
                mCurrentProvinceCode = provinceList.get(0).getCode();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    mCurrentCityCode = cityList.get(0).getCode();
                    List<DistrictModel> districtList = cityList.get(0)
                            .getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentDistrictCode = districtList.get(0).getCode();
                }
            }
//            Log.i("AddressPicker", "省的数量=" + provinceList.size());
            if (provinceList != null) {
                mProvinceDatasMap = new String[provinceList.size()];
            }
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据编码
                mProvinceDatasMap[i] = provinceList.get(i).getCode();
                // 存放省份的Code-Name
                mProvinceAllMap.put(provinceList.get(i).getCode(), provinceList.get(i).getName());
                // 获取城市列表
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityCode = new String[cityList.size()];
                HashMap<String, String> cityMaps = new HashMap<String, String>();
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据Code
                    cityCode[j] = cityList.get(j).getCode();
                    // Log.i("AddressPicker", "市===" + cityNames[j]);
                    // 存放城市的Code-Name
                    cityMaps.put(cityList.get(j).getCode(),
                            cityList.get(j).getName());
                    List<DistrictModel> districtList = cityList.get(j)
                            .getDistrictList();
                    String[] distrinctCodeArray = new String[districtList
                            .size()];
                    HashMap<String, String> distrinctMap = new HashMap<String, String>();
                    DistrictModel[] distrinctArray = new DistrictModel[districtList
                            .size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(
                                districtList.get(k).getName(), districtList
                                .get(k).getCode());
                        // Log.i("AddressPicker",
                        // "区/县==="+districtList.get(k).getName());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap 区的Code-Name
                        mDistrictCodeDatasMap.put(districtList.get(k).getCode(),
                                districtList.get(k).getName());
                        distrinctArray[k] = districtModel;
                        distrinctCodeArray[k] = districtModel.getCode();
                        distrinctMap.put(districtModel.getCode(),
                                districtModel.getName());
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    // Log.i("mCitisDatasMap",cityNames[j]+"================="+distrinctNameArray[j]);
                    mDistrictDatasMap.put(cityCode[j], distrinctCodeArray);
                    mDistrictAllMap.put(cityCode[j], distrinctMap);
                }
                // 省-市的数据，保存到mCitisDatasMap
                // Log.i("mCitisDatasMap",provinceList.get(i).getName()+"================="+cityNames[i]);
                mCityDatasMap.put(provinceList.get(i).getCode(), cityCode);
                mCityAllMap.put(provinceList.get(i).getCode(), cityMaps);
            }
        } catch (IOException e) {
            Log.e("IOException",e.getMessage());
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            Log.e("ParserConfigException",e.getMessage());
        } catch (SAXException e) {
            Log.e("SAXException",e.getMessage());
        } finally {

        }

    }

    /**
     * 判断市级数据是否为空
     */
    private boolean isCityDataEmpty() {
        if (mCityDatasMap != null && mCityDatasMap.size() != 0
                && mCityDatasMap.get(mCurrentProvinceCode) != null
                && mCityDatasMap.get(mCurrentProvinceCode).length != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断区/县数据是否为空
     */
    private boolean isDistrictDataEmpty(
            Map<String, String[]> mDistrictDatasMap, int pCurrent) {
        if (mDistrictDatasMap != null
                && mDistrictDatasMap.containsKey(mCitys[pCurrent])
                && mDistrictDatasMap.get(mCitys[pCurrent]).length != 0
                && mDistrictDatasMap != null
                && mDistrictDatasMap.get(mCurrentCityCode).length != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setAddressSelectedListener(AddressSelectedListener addressSelectedListener) {
        this.addressSelectedListener = addressSelectedListener;
    }

    public interface AddressSelectedListener {
        void onAddressSelectedListener(String address, String proviceCode, String cityCode, String districtCode);
    }

    /**
     * 判断字符串是否为空，即为null或""
     */
    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }
}
