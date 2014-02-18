package com.example.Milestone1;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by Руслан on 01.02.14.
 */
public class LicenseActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license);

        String html = "<html>\n" +
                "    <head>\n" +
                "        <meta charset=\"\"utf-16\"\"/>\n" +
                "        <meta name=\"\"viewport\"\" content=\"\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\"\" />\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        Соглашение об использовании информационного сервиса Edusty\n" +
                "        <br />(далее – «Соглашение» и «Edusty» соответственно)\n" +
                "        <br />Настоящее Соглашение разработано в целях урегулирования отношений между Администрацией Edusty и любым лицом, осуществляющим использование функциональных возможностей, инструментов и служб Edusty.\n" +
                "        <br />Пройдя процедуру авторизации в сервисе Edusty или любым иным способом используя функциональные возможности, инструменты и службы Edusty соответствующее лицо гарантирует, что ознакомлено с условиями Соглашения, а также совершает полное и безоговорочное принятие условий настоящего Соглашения.\n" +
                "        <br />Стороны соглашения\n" +
                "        <br />Администрация Edusty и лицо, размещающее информацию в данном сервисе, в дальнейшем Пользователь, заключили настоящее Соглашение о нижеследующем.\n" +
                "        <br />Предмет соглашения\n" +
                "        <br />Администрация предоставляет Пользователю право на размещение следующей информации в сервисе Edusty:\n" +
                "        <br />текстовой информации;\n" +
                "        <br />аудиоматериалов в формате, установленном Администрацией;\n" +
                "        <br />видеоматериалов в формате, установленном Администрацией;\n" +
                "        <br />фотоматериалов в формате, установленном Администрацией;\n" +
                "        <br />ссылок на материалы, размещённые на данном информационном ресурсе, либо на других информационных ресурсах.\n" +
                "        <br />Права и обязанности сторон\n" +
                "        <br />Пользователь имеет право:\n" +
                "        <br />свободно осуществлять поиск, получение, передачу, производство и распространение информации на информационном ресурсе Edusty любым законным способом;\n" +
                "        <br />комментировать предоставленную иными Пользователями информацию;\n" +
                "        <br />в любой момент потребовать от Администрации скрытия от прочих пользователей информационного ресурса Edusty всей предоставленной им информации, а также собственной учетной записи Пользователя;\n" +
                "        <br />Администрация имеет право:\n" +
                "        <br />по своему усмотрению создавать, изменять, отменять Соглашение как с, так и без предварительного уведомления Пользователя;\n" +
                "        <br />по своему усмотрению ограничить доступ к любой информации, размещённой Пользователем; удалить информацию, размещённую Пользователем, учётную запись Пользователя;\n" +
                "        <br />отказать в регистрации учётной записи Пользователю, учётная запись которого была ранее удалена за нарушение условий Соглашения.\n" +
                "        <br />Пользователь обязуется:\n" +
                "        <br />обеспечить достоверность предоставляемой информации;\n" +
                "        <br />при распространении информации, создателем которой Пользователь не является, обеспечить включение в её состав достоверных сведений об обладателе этой информации или об ином лице, распространяющем информацию, в форме и объёме, которые достаточны для идентификации такого лица;\n" +
                "        <br />не распространять информацию, которая направлена на пропаганду войны, разжигание национальной, расовой или религиозной ненависти и вражды, а также иной информации, за распространение которой предусмотрена уголовная или административная ответственность;\n" +
                "        <br />не нарушать работоспособность сервиса путём размещения информации, содержащей вирусы, создания дополнительной нагрузки на сервер, применением программ, не обусловленных функциональным содержанием информационного ресурса и иными способами.\n" +
                "        <br />Администрация обязуется:\n" +
                "        <br />осуществлять защиту учетной записи Пользователя от неправомерного доступа, уничтожения размещённой Пользователем информации, а также от иных неправомерных действий в отношении такой информации всеми находящимися в её распоряжении техническими средствами;\n" +
                "        <br />в случае размещения Пользователем информации, распространение которой ограничено или запрещено федеральными законами, либо вынести Пользователю предупреждение и удалить такую информацию, либо удалить учётную запись Пользователя, либо применить одну или несколько мер одновременно;\n" +
                "        <br />предоставить всю доступную информацию о Пользователе уполномоченным на то органам государственной власти в случаях, установленных законом.\n" +
                "        <br />Иные условия\n" +
                "        <br />Пользователь осознаёт и соглашается с тем, что размещённая им информация может быть использована поисковыми или другими автоматизированными сервисами и иными способами." +
                "        <br />Пользователь предоставляет Администрации неисключительную лицензию для использования, включая воспроизведение, распространение, переработку, публичный показ и доведение до всеобщего сведения материалов, размещенных Пользователем в рамках Edusty для публичного просмотра, в качестве материалов Edusty. Автор указанных в настоящем пункте материалов сохраняет за собой все имущественные и личные неимущественные авторские права, согласно международным конвенциям в сфере интеллектуального права, иным законодательным актам.\n" +
                "        <br />Администрация не несёт ответственность за несовпадение ожидаемых Пользователем и реально полученных услуг.\n" +
                "        <br />В случае возникновения форс-мажорной ситуации (боевые действия, чрезвычайное положение, стихийное бедствие и т. д.) Администрация не гарантирует сохранность информации, размещённой Пользователем, а также бесперебойную работу информационного сервиса.\n" +
                "        <br />Во всем ином, что не предусмотрено данным Соглашением стороны руководствуются действующим законодательством Российской Федерации.\n" +
                "        <br />Ответственность сторон\n" +
                "        <br />В случае, если Пользователем была размещена информация, распространение которой ограничивается или запрещается федеральными законами, нарушает авторское или иное защищаемое законом право, всю полноту ответственности за её размещение несёт Пользователь.\n" +
                "        <br />Edusty может содержать ссылки на сайты и сервисы в сети «Интернет» (сайты третьих лиц). Указанные третьи лица и их контент не проверяются Администрацией на соответствие тем или иным требованиям (достоверности, полноты, законности и т.п.). Администрация не несет ответственность за любую информацию, материалы, размещенные на сайтах или сервисах третьих лиц, к которым Пользователь получает доступ в процессе использования сервиса Edusty, а также за доступность таких сайтов и сервисов или последствия их использования Пользователем. Ссылка (в любой форме) на любой сайт или сервис, продукт, услугу, любую информацию коммерческого или некоммерческого характера, размещенная в рамках сервиса Edusty, не является одобрением или рекомендацией данных продуктов (услуг, деятельности) со стороны Администрации, за исключением случаев, когда на это прямо указывается Администрацией.\n" +
                "        <br />Конфиденциальность\n" +
                "        <br />Принимая настоящее Соглашение, Пользователь подтверждает свое согласие на обработку Администрацией его персональной информации (в том числе личной информации и персональных данных).\n" +
                "        <br />При использовании Пользователями сервиса Edusty, Администрация осуществляет сбор и обработку информации Пользователя, а именно:\n" +
                "        <br />информации, предоставляемой Пользователем в процессе пользования сервиса Edusty.\n" +
                "        <br />Администрация осуществляет обработку информации пользователя любыми способами с целью надлежащего предоставления Пользователю услуг (идентификация, аутентификация, передача информационных материалов по подписке пользователя, ответы на запросы и письма пользователя, а также другие действия, время от времени необходимые для надлежащего предоставления услуг).\n" +
                "        <br />Администрация использует обезличенные данные для проведения статистических исследований или для любых других целей.\n" +
                "        <br />При использовании Пользователями сервиса Edusty в коде приложений сервиса Edusty могут присутствовать коды третьих лиц, в результате чего такие третьи лица получают данные, указанные выше. Такими Интернет-ресурсами третьих лиц являются:\n" +
                "        <br />системы по сбору статистики использования приложения (например, Яндекс.Метрика).\n" +
                "        <br />Пользователь имеет все права по защите его персональной информации, предусмотренные действующим законодательством Российской Федерации, в частности, Федеральным Законом «О персональных данных».\n" +
                "        <br />Обработка персональной информации осуществляется в дата-центрах Windows Azure, где размещается оборудование, обеспечивающее функционирование сервиса Edusty. Вся информация хранится на условиях, предоставленных сервисом Windows Azure.\n" +
                "        <br />В соответствии с действующим законодательством Пользователь имеет право на отзыв согласия на обработку его персональных данных путем: самостоятельного удаления ранее размещенной Пользователем информации в сервисе Edusty; запроса на удаление персональных данных Пользователя, отправленного по адресу: sudalv92@hotmail.com" +
                "        <br />Раскрытие предоставленной Пользователем информации может быть произведено лишь в соответствии с действующим законодательством по требованию суда, правоохранительных органов, в иных предусмотренных законодательством случаях.\n" +
                "    </body>\n" +
                "</html>\"";

        TextView tvLicense = (TextView) findViewById(R.id.tvLicense);
        tvLicense.setText(Html.fromHtml(html));

    }
}