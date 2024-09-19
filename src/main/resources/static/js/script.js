console.log("this is script file");

  const toggleSidebar = () => {
        if ($(".sidebar").is(":visible")) {
				//ture 
	//band karna hai
	
			
            $(".sidebar").css("display","none");
            $(".content").css("margin-left","0%");
        } else {
			//false
		//show karna hai
            $(".sidebar").css("display","block");
            $(".content").css("margin-left","20%");
        }
   
};

const search=()=>
{
	let query=$("#search-input").val();
	
	if(query == "")
	{
		$(".search-result").hide();
	}
	else
	{
		console.log(query);
		
		//sending request to server
		let url=`http://localhost:8080/search/${query}`;
		
		fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>
		{
			
			console.log(data);
			//data..
			
			let text=`<div class='list-group'> `;
			
			data.forEach((contact)=>{
				text+=`<a href='#' class='list-group-item list-group-item-action'> ${contact.name} </a> `
			});
			
			
			text += `</div>`;
			
			$(".search-result").html(text);
			$(".search-result").show();
			
		});
		
		
		/*$(".search-result").show();*/
	}
	

};



//first request to server to create order

const paymentStart = () => {
    console.log("Payment started ...");
    let amount = $("#payment_field").val();
    console.log(amount);
    if (amount === '' || amount === null) {
        Swal.fire("Oops !!!", "Amount is required ...", "error");
        return;
    }
    $.ajax({
        url: '/user/create_order',
        data: JSON.stringify({ amount: amount, info: 'order_request' }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function (response) {
			//invode when success
            console.log(response);
            
            if (response.status === 'created') {
				let options = {
					key: 'rzp_test_pZYglnNLZmN15r',
					amount: response.amount,
					currency: 'INR',
					name: 'Smart Contact Manager',
					description: 'Donation',
					image: "https://images.app.goo.gl/nakVcLEhqxKdhtYKA",
					order_id: response.id,
					handler: function (response) {
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						console.log("Payment Successful !!!");
						//alert("Congrats, Payment successful ...");
						//updatePaymentOnServer(response.razorpay_payment_id, response.razorpay_order_id, "paid");
						swal("Congrats !!!", "Payment successful...", "success");
					},
					prefill: {
						name: "",
						email: "",
						contact: ""
					},
					notes: {
						address: "Varanasi, luxmikund",
					},
					theme: {
						color: "#3399cc"
					}
				};
				let rzp1 = new Razorpay(options);
				rzp1.on('payment.failed', function (response) {
					console.log(response.error.code);
					console.log(response.error.description);
					console.log(response.error.source);
					console.log(response.error.step);
					console.log(response.error.reason);
					console.log(response.error.metadata.order_id);
					console.log(response.error.metadata.payment_id);
					console.log("Payment failed !!!");
					// alert("Payment Failed !!!");
					swal("Oops !!!", "Payment failed ...", "error");
				});
				rzp1.open();
			}
		
            
            
        },
        error: function (error) {
            console.log(error);
            Swal.fire("Oops !!!", "Something went wrong ...", "error");
        }
    });
}
