import com.platform.yeepay.utils.PaymobileUtils;

import java.util.TreeMap;

public class Test {


        public static void main(String[] args) {
            //{data=8ifDf0H3pUTDYN7qa4hLomEHsqZx69MvxVVHWytq/AD0n4m7myyrO7xUc2nfsCSCURhMyqgwiwvaDgdOsGjjdnGVbPKUsy1U4d+UV4ybxv4N24iht9KyixoOOdhkTL8N9kjNLgHC8EP6tKb7KmojrHmYFYQZU+/GidM06tgjZqOkujtA3JAMoWQPf2hLxKpftSvqOYiaMARb48Rx57wKoUbN+wiU7srsDPSKQLGYtS2p0KGpLlH2NdOp+TlR9MI6MGuj+O8OIsDO1VUXQqef/jBGfM8Fl44EQiGI28zpjsBt6NLnJ2K4vij4+4ih2iBnCPSmcNq82F8ZDXjtU0uftRi/CuL33R+TUwcJNXTFQApqlxB7BCybIlfopsqjEJ7Pi/xV7BGPsYf/PYG2zTgFO/F8BGg9e3yxJDrlB4+YITZW11s9j59ezO0Q2lseBC80VVo6CSyNZqwmv5LfwCWrM0nE68OnU8ltje5J7YVSTs/E6r+iEAaDexw2f5Y+57El, encryptkey=LJpNMmEHtA11CZQq02t+eXwNYVBP7EUvXFoh+CBMTfQ4z3XRYZz7aMagUfxgKmwGab8G5Ap+9lScJ3T/EU+89rSd/oYurYcmQ+L8wL77X5G9o3vG8SdpAQYZZmlDlMUYehmylt7a2SCKNbGg5L25Z+wBpG+CjtLuze97/3L6lg0=}

            String data = "8ifDf0H3pUTDYN7qa4hLomEHsqZx69MvxVVHWytq/AD0n4m7myyrO7xUc2nfsCSCURhMyqgwiwvaDgdOsGjjdnGVbPKUsy1U4d+UV4ybxv4N24iht9KyixoOOdhkTL8N9kjNLgHC8EP6tKb7KmojrHmYFYQZU+/GidM06tgjZqOkujtA3JAMoWQPf2hLxKpftSvqOYiaMARb48Rx57wKoUbN+wiU7srsDPSKQLGYtS2p0KGpLlH2NdOp+TlR9MI6MGuj+O8OIsDO1VUXQqef/jBGfM8Fl44EQiGI28zpjsBt6NLnJ2K4vij4+4ih2iBnCPSmcNq82F8ZDXjtU0uftRi/CuL33R+TUwcJNXTFQApqlxB7BCybIlfopsqjEJ7Pi/xV7BGPsYf/PYG2zTgFO/F8BGg9e3yxJDrlB4+YITZW11s9j59ezO0Q2lseBC80VVo6CSyNZqwmv5LfwCWrM0nE68OnU8ltje5J7YVSTs/E6r+iEAaDexw2f5Y+57El";
            String encryptkey = "LJpNMmEHtA11CZQq02t+eXwNYVBP7EUvXFoh+CBMTfQ4z3XRYZz7aMagUfxgKmwGab8G5Ap+9lScJ3T/EU+89rSd/oYurYcmQ+L8wL77X5G9o3vG8SdpAQYZZmlDlMUYehmylt7a2SCKNbGg5L25Z+wBpG+CjtLuze97/3L6lg0=";
            TreeMap<String, String> dataMap = PaymobileUtils.decrypt(data, encryptkey);

            System.out.println(dataMap);
            boolean b = PaymobileUtils.checkSign(dataMap);

            System.out.println(b);

        }

}
