package pl.michalgorski.weatherinfo.models.services;

import org.springframework.stereotype.Service;
import pl.smsapi.OAuthClient;
import pl.smsapi.api.SmsFactory;
import pl.smsapi.api.action.sms.SMSSend;
import pl.smsapi.api.response.StatusResponse;
import pl.smsapi.exception.ClientException;
import pl.smsapi.exception.SmsapiException;


@Service
public class SmsService {

    public boolean sendSms(String number, String content){

        try {
            OAuthClient client = new OAuthClient("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

            SmsFactory smsApi = new SmsFactory(client);
            String phoneNumber = number;
            SMSSend action = smsApi.actionSend()
                    .setText(content)
                    .setTo(phoneNumber);

            StatusResponse result = action.execute();

        } catch (ClientException e) {
            /**
             * 101 Niepoprawne lub brak danych autoryzacji.
             * 102 Nieprawidłowy login lub hasło
             * 103 Brak punków dla tego użytkownika
             * 105 Błędny adres IP
             * 110 Usługa nie jest dostępna na danym koncie
             * 1000 Akcja dostępna tylko dla użytkownika głównego
             * 1001 Nieprawidłowa akcja
             */
            e.printStackTrace();
        } catch (SmsapiException e) {
            e.printStackTrace();
        }
        return true;
    }
}
