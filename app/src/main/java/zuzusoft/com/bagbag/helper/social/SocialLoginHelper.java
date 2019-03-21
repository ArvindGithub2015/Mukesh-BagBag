package zuzusoft.com.bagbag.helper.social;

import android.app.Activity;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by mukeshnarayan on 05/12/18.
 */

public class SocialLoginHelper {

    private FacebookLoginHelper fbHelper;

    private GoogleLoginHelper googleHelper;

    public interface SocialLoginListeners{

        void onSuccessFb();
        void onCancelFb();
        void onErrorFb();
        void onSuccessGoogle();
        void onErrorGoogle();
    }

    public SocialLoginHelper(Activity activity, CallbackManager callbackManager){

        fbHelper = new FacebookLoginHelper(activity, callbackManager);
        googleHelper = new GoogleLoginHelper(activity);

    }

    public void loginWithFb(){

        fbHelper.loginFb();
    }

    public void loginWithGoogleStep1(){

        googleHelper.loginGoogle();
    }

    public void loginWithGoogleStep2(GoogleSignInAccount account){

        googleHelper.firebaseAuthWithGoogle(account);
    }

}
