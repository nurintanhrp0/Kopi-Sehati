package ventures.g45.kopisehati;

import android.app.Application;
import android.graphics.Typeface;

/*import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;*/

public class KopiSehati extends Application {

    public static final String TAG = KopiSehati.class.getSimpleName();

    /*private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;*/
    private static KopiSehati mInstance;

    private String url, urlData, mUrl;
    private Typeface MontserratBlack, MontserratBlackItalic, MontserratBold, MontserratBoldItalic, MontserratExtraBold;
    private Typeface MontserratExtraBoldItalic, MontserratExtraLight, MontserratExtraLightItalic, MontserratItalic;
    private Typeface MontserratLight, MontserratLightItalic, MontserratMedium, MontserratMediumItalic, MontserratRegular;
    private Typeface MontserratSemiBold, MontserratSemiBoldItalic, MontserratThin, MontserratThinItalic;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        setUrl();
        setUrlData();
        setmUrl();
        setMontserratBlack();
        setMontserratBlackItalic();
        setMontserratBold();
        setMontserratBoldItalic();
        setMontserratExtraBold();
        setMontserratExtraBoldItalic();
        setMontserratExtraLight();
        setMontserratExtraLightItalic();
        setMontserratItalic();
        setMontserratLight();
        setMontserratLightItalic();
        setMontserratMedium();
        setMontserratMediumItalic();
        setMontserratRegular();
        setMontserratSemiBold();
        setMontserratSemiBoldItalic();
        setMontserratThin();
        setMontserratThinItalic();
    }

    public static synchronized KopiSehati getInstance() {
        return mInstance;
    }

    /*public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }*/

    public String getUrl() {
        return url;
    }

    public void setUrl() {
        this.url = "https://kopi.g45lab.xyz/api/";
    }

    public void setUrlData() {
        this.urlData = "https://datakopi.g45lab.xyz/";
    }

    public String getUrlData() {
        return urlData;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl() {
        this.mUrl = "https://kopi.g45lab.xyz/";
    }

    public void setMontserratBlack() {
        this.MontserratBlack = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Black.ttf");
    }

    public Typeface getMontserratBlack() {
        return MontserratBlack;
    }

    public void setMontserratBlackItalic(){
        this.MontserratBlackItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-BlackItalic.ttf");
    }

    public Typeface getMontserratBlackItalic() {
        return MontserratBlackItalic;
    }

    public void setMontserratBold(){
        this.MontserratBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
    }

    public Typeface getMontserratBold() {
        return MontserratBold;
    }

    public void setMontserratBoldItalic(){
        this.MontserratBoldItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-BoldItalic.ttf");
    }

    public Typeface getMontserratBoldItalic(){
        return MontserratBoldItalic;
    }

    public void setMontserratExtraBold() {
        this.MontserratExtraBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-ExtraBold.ttf");
    }

    public Typeface getMontserratExtraBold() {
        return MontserratExtraBold;
    }

    public void setMontserratExtraBoldItalic(){
        this.MontserratExtraBoldItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-ExtraBoldItalic.ttf");
    }

    public Typeface getMontserratExtraBoldItalic(){
        return MontserratExtraBoldItalic;
    }

    public void setMontserratExtraLight(){
        this.MontserratExtraLight = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-ExtraLight.ttf");
    }

    public Typeface getMontserratExtraLight(){
        return MontserratExtraLight;
    }

    public void setMontserratExtraLightItalic(){
        this.MontserratExtraLightItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-ExtraLightItalic.ttf");
    }

    public Typeface getMontserratExtraLightItalic(){
        return MontserratExtraLightItalic;
    }

    public void setMontserratItalic() {
        this.MontserratItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Italic.ttf");
    }

    public Typeface getMontserratItalic(){
        return MontserratItalic;
    }

    public void setMontserratLight(){
        this.MontserratLight = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Light.ttf");
    }

    public Typeface getMontserratLight() {
        return MontserratLight;
    }

    public void setMontserratLightItalic(){
        this.MontserratLightItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-LightItalic.ttf");
    }

    public Typeface getMontserratLightItalic(){
        return MontserratLightItalic;
    }

    public void setMontserratMedium(){
        this.MontserratMedium = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Medium.ttf");
    }

    public Typeface getMontserratMedium(){
        return MontserratMedium;
    }

    public void setMontserratMediumItalic(){
        this.MontserratMediumItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-MediumItalic.ttf");
    }

    public Typeface getMontserratMediumItalic(){
        return MontserratMediumItalic;
    }

    public void setMontserratRegular(){
        this.MontserratRegular = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
    }

    public Typeface getMontserratRegular(){
        return MontserratRegular;
    }

    public void setMontserratSemiBold(){
        this.MontserratSemiBold = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBold.ttf");
    }

    public Typeface getMontserratSemiBold(){
        return MontserratSemiBold;
    }

    public void setMontserratSemiBoldItalic(){
        this.MontserratSemiBoldItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-SemiBoldItalic.ttf");
    }

    public Typeface getMontserratSemiBoldItalic(){
        return MontserratSemiBoldItalic;
    }

    public void setMontserratThin(){
        this.MontserratThin = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Thin.ttf");
    }

    public Typeface getMontserratThin(){
        return MontserratThin;
    }

    public void setMontserratThinItalic() {
        this.MontserratThinItalic = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-ThinItalic.ttf");
    }

    public Typeface getMontserratThinItalic(){
        return MontserratThinItalic;
    }
}
