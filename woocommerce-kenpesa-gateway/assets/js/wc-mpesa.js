function test() {
    alert("xxx");
}
jQuery(document).ready(function ($) {

    $("#btnRequest").click(function () {
        $('#btnRequest').attr('value', 'Sending....');

        $.ajax({
            type: "POST",
            url: kenpesa_ajaxurl,
            dataType: "json",
            data: {
                action: 'stk_request',
                phoneno: $("#phoneno").val(),
                order_id: $("#order_id").val(),
            },
            success: function (response) {
                //if request if made successfully then the response represent the data

                if (typeof response.ResponseCode != 'undefined' && response.ResponseCode == 0) {
                    $('#CheckoutRequestID').val(response.CheckoutRequestID);
                    $("#result").html('<div class="success">MPESA Payment request has been sent to your phone successfully. Check and enter your M-PESA PIN.</div>');
                } else {
                    $("#result").html('<div class="error">' + response.errorMessage + '</div>');
                }

               /* $('html, body').animate({
                    scrollTop: $("#result").offset().top
                }, 2000);*/

                $('#btnRequest').attr('value', 'Try again.');
            }
        });
    });

    setInterval(function () {
        var cid = '';
        if ($("#CheckoutRequestID").length) {
            var cid = $("#CheckoutRequestID").val();
            if (cid.length == 0)
                return;
        }else{
            return;
        }
        $.ajax({
            type: "POST",
            url: kenpesa_ajaxurl,

            data: {
                cid: cid,
                action: 'check_stk',
                order_id: $("#order_id").val(),
            },
            success: function (response) {
                //if request if made successfully then the response represent the data

//alert(response);

                if (typeof response.ResultCode != 'undefined' && response.ResultCode == 0) {
                    $("#result").html('<div class="success">' + response.ResultDesc + '. Redirecting...</div>');

                    window.location.replace(response.redirect);
                } else if (typeof response.errorMessage != 'undefined') {
                    $("#result").html('<div class="error">' + response.errorMessage + '</div>');
                    $("#CheckoutRequestID").val("");
                } else if (response.ResultCode == null) {
                    //nothing - waiting for stk confirm
                } else {
                    $("#result").html('<div class="error">' + response.ResultDesc + '</div>');
                    $("#CheckoutRequestID").val("");
                }

            }
        });
    }

    , 5000);
});

function enableForm() {
    $("#myForm :input").prop("disabled", true);
}


function changePayMtd() {
  window.history.back();
}