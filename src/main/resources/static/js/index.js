photo=document.querySelectorAll(".gallery .container div")
let curImg=0

let kolImgs=photo.length
// console.log(photo)
for(let i=0;i<photo.length;i++){
    photo[i].onclick=function(){
        console.log(document.querySelector(".carousel"))
        document.querySelector(".carousel").style="display: flex;"
        document.body.style.overflow="hidden"
        curImg=i
        document.querySelector(".imgs .container").style.left=curImg*(-100)+"%"

    }
}
console.log(kolImgs)
console.log(document.querySelector(".carousel .right-arrow"))
document.querySelector(".carousel .right-arrow").onclick=toRight
document.onkeydown=checkKey
function checkKey(e) {
    if (e.keyCode == '37') {
        toLeft()

    }
    else if (e.keyCode == 39) {
       toRight()
    }

}

function toRight(){
    if(curImg===kolImgs-1){
        curImg=0;

    }
    else{
        curImg++;
    }
    document.querySelector(".imgs .container").style.left=curImg*(-100)+"%"
}
document.querySelector(".carousel .exit").onclick=function(){
    document.querySelector(".carousel").style="display: none;"
        document.body.style.overflow="scroll"
}
document.querySelector(".carousel .left-arrow").onclick=toLeft

function toLeft(){
    if(curImg===0){
        curImg=kolImgs-1;

    }
    else{
        curImg--;
    }
    document.querySelector(".imgs .container").style.left=curImg*(-100)+"%"
}
openMenu=0
document.querySelector(".menu-burger").onclick=function (){
    if(openMenu){
        document.querySelector(".navigation").style.top='-200%'
        document.querySelector(".menu-burger .center").style=''
        document.querySelector('.menu-burger .after').style=''
        document.querySelector('.menu-burger .before').style=''
        document.body.style.overflow='visible'
    }
    else {
        document.querySelector(".navigation").style.top='0'
        document.querySelector(".menu-burger .center").style.display="none"
        document.querySelector('.menu-burger .after').style.transform='rotate(-45deg)'
        document.querySelector('.menu-burger .before').style.transform='rotate(45deg)'
        document.querySelector('.menu-burger .before').style.height='5px'
        document.querySelector('.menu-burger .after').style.height='5px'
        document.querySelector('.menu-burger .after').style.top='15px'
        document.querySelector('.menu-burger .before').style.top='15px'
        document.querySelector('.menu-burger .after').style.backgroundColor='red'
        document.querySelector('.menu-burger .before').style.backgroundColor='red'
        document.querySelector('.menu-burger .before').style.backgroundColor='red'
        document.body.style.overflow='hidden'

    }
    openMenu=!openMenu
}