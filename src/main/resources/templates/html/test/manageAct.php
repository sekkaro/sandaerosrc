<?php include('headandmenu.php')?>

<div id="main">
  <h4>활동 목록</h4>
  <form id="manageForm" name="manage" action="index.html" enctype="multipart/form-data" method="post">
    <div class="bar">
      <div class="heading">활동 A</div>
      <img id="tag" src="./img/10.png"></img>
      <button class=" inlinebox edit">편집</button>
    </div>
    <div id="actManage">
      <div class="row1">
        <div class="endtimemenu inlinebox">
        <div class="inlinebox">
        <div class="sub_heading">카테고리</div>
      </div>
        <input type="text" id="text" name="category" placeholder="카테고리">
      </div>
<div class="endtimemenu inlinebox">
  <div class="inlinebox">
  <div class="sub_heading">마감기한</div>
  </div>
  <div class="inlinebox">
  <input type="date" id="enddate" name="enddate">
  <input type="time" id="endtime" name="endtime"></label>
</div>
</div>
<button class="edit">
  편집
</button>
        </div>
        <hr class="line">
      <div class="sub_contentbox">
        <div class="sub_heading">담당 인원</div><button class="edit">편집</button>
        <div class="leftmenu">
         <div class="inlinebox">
          <div class="menu_name">이용자&emsp;&emsp;</div>
        </div>
          <div class="inlinebox">
          <div class="members user">
            이용자 010-0000-0000
          </div>
          </div>
          <br>
           <div class="inlinebox">
          <div class="menu_name">담당 관제사 </div>
        </div>
        <div class="inlinebox">
        <div class="members charge">
          이용자 010-0000-0000
        </div>
        </div>
        </div>
        <div class="rightmenu2">

          <div class="menu_name workertext">봉사자</div>

          <div class="workerblock">
            <div class="inlinebox">
            <div class="members worker">
              이용자 010-0000-0000
            </div>
            </div>
            <div class="inlinebox">
            <div class="members worker">
              이용자 010-0000-0000
            </div>
            </div>
            <div class="inlinebox">
            <div class="members worker">
              이용자 010-0000-0000
            </div>
            </div>
            <div class="inlinebox">
            <div class="members worker">
              이용자 010-0000-0000
            </div>
            </div>
            <div class="inlinebox">
            <div class="members worker">
              이용자 010-0000-0000
            </div>
            </div>
          </div>
        </div>
      </div>
      <hr class="line">
      <div class="sub_contentbox">
        <div class="sub_heading">활동 내용</div><button class="edit">
          편집
        </button>
        <div class="leftmenu">
          <div class="inlinebox">
          <label for="birthday"><div class="menu_name">활동 시간&emsp;</div>
          </div>
          <div class="inlinebox">
          <input type="date" id="startdate" name="startdate">
          <input type="time" id="startdate" name="startdate"></label>
        </div>
        <span id="sign">~</span>
          <div class="inlinebox">
          <input type="date" id="enddate" name="enddate">
          <input type="time" id="endtime" name="endtime"></label>
        </div>
        <div class="explanationbox">
          <div class="inlinebox"><div class="menu_name explain">상세설명&emsp;</div></div>
            <div class="inlinebox explain_textarea">
               <input type="textarea" id="explianation" name="explanation">
            </div>
        </div>
        </div>

        <div class="rightmenu">
          <div class="search_box">
            <div class="inlinebox">
            <label for="title"><div class="menu_name">장소</div>
            </div>
            <div class="inlinebox searchbox">
              <input type="search" id="search" placeholder="포항시 북구 장량동 " />
            </div>
        </div>
        <div class="map">

        </div>
        </div>
        </div>
        <hr class="divgap2">
    </div>
    <div id="buttonSet">
      <button id="submit" type="submit" name="button"><img src="./img/29.png"></img></button>
    </div>
  </form>
</div>
</body>

</html>
