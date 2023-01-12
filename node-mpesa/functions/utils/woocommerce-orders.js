const WoocommerceApi= require("woocommerce-api");

const woocommerce= new WoocommerceApi({
    url: 'https://pastpaperskenya.com',
    consumerKey: 'ck_8e9ff8201a893a0d680ae07a33057fc65a1d36f4',
    consumerSecret : 'cs_7699e098fa72f4447f3acab26ad4650881186668',
    wpAPI: true,
    version: 'wc/v3'
});



function updateOrder(endpoint){

    const data= {
        status: "completed"
    };
    
    woocommerce.putAsync(`orders/${endpoint}`, data)
    .then((response) =>{
        console.log(response.data);
    })
    .catch((error)=>{
        console.log(`An error occured `+ error.data);
    });
}

module.exports= {updateOrder};