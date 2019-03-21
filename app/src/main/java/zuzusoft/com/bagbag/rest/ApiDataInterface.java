package zuzusoft.com.bagbag.rest;

import java.util.HashMap;
import java.util.List;

import zuzusoft.com.bagbag.closet.model.Response;
import zuzusoft.com.bagbag.get_start.model.RegisterResponse;
import zuzusoft.com.bagbag.home.models.brand.Brand;
import zuzusoft.com.bagbag.home.models.gold.GoldBag;

/**
 * Created by mukeshnarayan on 08/08/18.
 */

public interface ApiDataInterface {

    void onProfileData(RegisterResponse response);

    void onAddBagData(Response response);

    void onAddBagImageData(Response response);

    void onDeleteBagImage(Response response);

    void onBagDelete(boolean isDelete);

    void onBagChangeRequestSend(boolean status);


    interface PetitionListener{

        void onPetitionAccept(boolean status, HashMap<String, String> dataBag);
    }

    interface FetchGoldListener{

        void onFetchGold(List<GoldBag> goldBags);
        void onFetchBrand(List<Brand> goldBags);
    }
}
