
function setLanguageLoad(lng, nsArray, defaultNs){
    i18next
        .use(window.i18nextXHRBackend)
        .use(window.i18nextBrowserLanguageDetector)
        .init({
            lng: lng, //evtl. use language-detector https://github.com/i18next/i18next-browser-languageDetector
            fallbackLng: 'en',
            debug: false,
            ns: nsArray, //['feed']
            defaultNS: defaultNs, //'feed'
            allowMultiLoading: true,
            backend: {
                //loadPath: 'https://s3.ap-northeast-2.amazonaws.com/test.m.plathome.com/i18n/{{lng}}/{{ns}}.json',
                loadPath: '/i18n/{{lng}}/{{ns}}.json',
                crossDomain: true
            }
            // ,
            // interpolation: {
            //     escapeValue: false
            // }
        }, function(err, t) {
            console.log(err);
            jqueryI18next.init(i18next, $, {useOptionsAttr: true});
            $(document).localize();

        });
}

function updateContent() {
    var lng = i18next.language;
    moment.locale(lng);
    console.log(lng);
}

function changeLng(lng) {
    i18next.changeLanguage(lng);
}

i18next.on('languageChanged', function(){
    updateContent();
});