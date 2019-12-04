$(function(){
        var refreshTime=20000;
        var setTime=setInterval(connOper, refreshTime);
        function connOper(){
              $.ajax({
              url:addr+"pipeline/clientKeepConnection",
              async:false,
              type:"get",
              dataType:"text",
              success:function(result){
                   if(result=='success'){
                      window.location.href=addr+"animation/toGasTank";
                   }
              }
            });
        }
});

