function callbackDataFunc(callbackData){

    const data= {};

    if(callbackData.ResultCode== 0){

        data.merchantRequestID= callbackData.MerchantRequestID;
        data.checkoutRequestID= callbackData.CheckoutRequestID;
        data.resultCode= callbackData.ResultCode;
        data.resultDesc= callbackData.ResultDesc;

        callbackData.CallbackMetadata.Item.forEach(element => {
            switch(element.Name){
                case "Amount":
                    data.amount= element.Value;
                    break;
                case "MpesaReceiptNumber":
                    data.mpesaReceiptNumber= element.Value;
                    break;
                case "TransactionDate":
                    data.transactionDate= element.Value;
                    break;
                case "PhoneNumber":
                    data.phoneNumber= element.Value;
                    break;
            }
        });
    }
    return data;
}

module.exports= {callbackDataFunc};