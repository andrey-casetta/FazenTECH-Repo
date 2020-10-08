function ChangeSelect() {
    var div = (document.getElementById("fatherDiv"));
    var son = div.firstElementChild;
    son.className = son.className == "visible" ? "invisible" : "visible";

    var son2 = div.children[1];
    son2.className = son2.className == "visible" ? "invisible" : "visible";
}