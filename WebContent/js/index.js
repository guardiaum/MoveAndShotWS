/**
 * 
 */
var map = null;
var marker = null;
var markers = [];
var clickListener = null;
var dialog = null;
var confirmDialog = null;
var dialogoRemocaoSucesso = null;
var dialogCarregaFoto = null;
var dialogoMensagemShotArea = null;
var form = null;
var flagSaved = false;
var poiParaRemocao = null;
var poiAdicionarFoto = null;
var poiAdicionarShotArea = null;
var coordinates = [];
var points = [];
var polygon = null;
var line = null;
var lines = []
var firstDraw = true;
var listenerColocaMarcadorDeArea = null;
var listenerPrimeiroMarcadorDeArea = null;
var poisAreas = [];

$(function() {
	dialog = $("#dialog-form").dialog({
		autoOpen : false,
		height : 350,
		width : 350,
		modal : true,
		buttons : {
			"Criar um POI" : addPOI,
			Cancel : function() {
				marker.setMap(null);
				dialog.dialog("close");
			}
		},
		close : function() {
			if(!flagSaved){
				marker.setMap(null);
			}else{
				flagSaved = false;
			}
			form[0].reset();
		}
	});

	form = dialog.find("form").on("submit", function(event) {
		addPOI();
	});
	
	confirmDialog = $( "#dialog-confirm" ).dialog({
		autoOpen : false,
		resizable: false,
		height: 230,
		width : 380,
		modal: true,
		buttons: {
			"Deletar": function() {
				if(poiParaRemocao!=null){
					//alert(poiParaRemocao);
					$.ajax({
						type : "POST",
						dataType: "json",
						contentType: "application/json",
						url : URL + "services/pois/delete_poi/",
						data : {poi_id:poiParaRemocao},
						success : function(data, status) {
							//alert("areas length:"+poisAreas.length+"| markers:"+markers.length);
							for (var i = 0; i < poisAreas.length; i++) {
								if(poisAreas[i]!=null){
									if(poisAreas[i].get("id")==poiParaRemocao){
										poisAreas[i].setMap(null);
										delete poisAreas[i];
									}
								}
							}
							for (var index = 0; index < markers.length; index++) {
								if(markers[index]!=null){
									if(markers[index].get("id")==poiParaRemocao){
										markers[index].setMap(null);
										delete markers[index];
									}
								}
							}
							confirmDialog.dialog( "close" );
							dialogoRemocaoSucesso.dialog("open");
						},
						error : function OnExecuteError(request, status, error) {
							alert("Erro! " + error);
						}
					});
				}
			},
		Cancel: function() {confirmDialog.dialog( "close" );}
		}
	});
	
	 dialogoRemocaoSucesso = $( "#dialog-sucess-message" ).dialog({
		 autoOpen : false,
		 modal: true,
		 buttons: {
			 Ok: function() {
				 $( this ).dialog( "close" );
				 }
			 }
	 });
	
	dialogCarregaFoto = $( "#dialog-upload-image" ).dialog({
		autoOpen : false,
		width : 200,
		Cancel: function() {
			$("#imageFile").attr("value","");
			$("#avatar").attr("src",'""');
			$("#avatar").hide();
		}
	});
	
	$('#file_upload').uploadify({
		'buttonText' : 'Selecionar',
		'swf' : 'swf/uploadify.swf',
		'uploader' : URL+'services/pois/sendImage',
		'fileObjName' : 'file',
		'fileTypeExts' : '*.jpg; *.png',
		'cancelImg' : 'img/uploadify-cancel.png',
		'multi' : false,
		'onUploadStart' : function(file) {
			$("#file_upload").uploadify("settings", "formData", {
				'poi_id' : poiAdicionarFoto
			});
		},
		'onUploadSuccess' : function(file, data, response) {
			$.ajax({
				type : "GET",
				dataType: "json",
				contentType: "application/json",
				url : URL + "services/pois/getPoiById/",
				data: {poi_id: poiAdicionarFoto},
				success : function(data, status) {
					$("#avatar").attr("src",data.imageAddress);
					$("#avatar").show();
					$("#img-confirmation").text("Image Sent!");
					for (var index = 0; index < markers.length; index++) {
						if(markers[index].get("id")==poiAdicionarFoto){
							var m = markers[index];
							markers[index].info.close();
							markers[index].setMap(null);
							
							m.info = null;
							
							m = criarBalaoDeInformacao(m, data.name, data.type, data.imageAddress);
							google.maps.event.addListener(m, 'click', function() {
								m.info.open(map, m);
							});
							
							m.setMap(map);
							markers[index] = m;
						}
					}					
					
					//dialogCarregaFoto.dialog( "close" );
				},
				error : function OnExecuteError(request, status, error) {
					$("#img-confirmation").text("Erro!");
					alert("Erro recuperando imagem avatar" + error);
				}
			});
		},
		'onUploadError' : function(file, errorCode, errorMsg, errorString) {
            alert('The file ' + file.name + ' could not be uploaded: ' + errorString);
        }
	});
	
	dialogoMensagemShotArea = $("#dialog-message-shot-area").dialog({
		autoOpen : false,
		modal: true,
		buttons: {
			Ok: function() {
				$( this ).dialog( "close" );
				adicionarShotAreaAoPOI(poiAdicionarShotArea);
			}
		}
	});
	
	$("#btAddPOI").button().click(
			function(event) {
				clickListener = google.maps.event.addListener(map, 'click',
						function(event) {
							marker = colocaMarcador(event.latLng);
							google.maps.event.removeListener(clickListener);
							dialog.dialog("open");
						});
			});
});

function initAutocomplete() {
	var input = document.getElementById('search-bar');
	var searchBox = new google.maps.places.SearchBox(input);
    
	map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
    
    // Bias the SearchBox results towards current map's viewport.
    map.addListener('bounds_changed', function() {
      searchBox.setBounds(map.getBounds());
    });

    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();

        if (places.length == 0) {
          return;
        }

        // For each place, get the icon, name and location.
        var bounds = new google.maps.LatLngBounds();
        places.forEach(function(place) {
          if (!place.geometry) {
            console.log("Returned place contains no geometry");
            return;
          }
          var icon = {
            url: place.icon,
            size: new google.maps.Size(71, 71),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(17, 34),
            scaledSize: new google.maps.Size(25, 25)
          };

          if (place.geometry.viewport) {
            // Only geocodes have viewport.
            bounds.union(place.geometry.viewport);
          } else {
            bounds.extend(place.geometry.location);
          }
        });
        
        var zoomLevel = map.getZoom();
        map.fitBounds(bounds);
        map.setZoom(zoomLevel);
      });
}

function loadMap() {
	var myOptions = {
		zoom : 20,
		mapTypeId: 'satellite'
	};

	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var pos = {
		              lat: position.coords.latitude,
		              lng: position.coords.longitude
		            };
			map.setCenter(pos);
		}, function() {
			handleNoGeolocation(true);
		});
	} else {
		handleNoGeolocation(false);// Browser doesn't support Geolocation
	}
	
	carregaPontosDeInteresse();
	initAutocomplete();
}

function handleNoGeolocation(errorFlag) {
	if (errorFlag) {
		var content = 'Erro: O serviço de geolocalização falhou.';
	} else {
		var content = 'Erro: Seu navegador não suporta geolocalização.';
	}
	var options = {
		map : map,
		position : new google.maps.LatLng(60, 105),
		content : content
	};
	var infowindow = new google.maps.InfoWindow(options);
	map.setCenter(options.position);
}

function carregaPontosDeInteresse(){
	//alert(URL);
	$.ajax({
		type : "GET",
		dataType: "json",
		contentType: "application/json",
		url : URL+"services/pois/getPois/",
		success : function(data, status) {
			$.each(data, function(index, poi){
				
				var myLatLng = new google.maps.LatLng(poi.poiCoordinate.latitude, poi.poiCoordinate.longitude);
				var nome = poi.name;
				var tipo = poi.type;
				var id = poi.id;
				var img = poi.imageAddress; 
					
				var m = colocaMarcador(myLatLng);
				
				m.set("id",id);
				
				m = criarBalaoDeInformacao(m, nome, tipo, img);
				
				google.maps.event.addListener(m, 'click', function() {
					m.info.open(map, m);
				});
				
				markers.push(m);
				if(poi.shotAreaPolygonSt != null){
					addPolygon(poi.id, poi.name, poi.type, poi.shotAreaPolygonSt);
				}
				/*if (poi.shotImagesAreaSt != null) {
					addPolygon(poi.id, poi.name, poi.type, poi.shotImagesAreaSt);
				}*/
			});
		},
		error : function OnExecuteError(request, status, error) {
			alert("Erro! " + error + request + status);
		}
	});
}

function addPolygon(id, name, type, polygon) {
	var points = getPoints(polygon);
	var poiArea = new google.maps.Polygon({
		paths : points,
		strokeColor : "#FF9900",
		strokeOpacity : 0.8,
		strokeWeight : 3,
		fillColor : "#FF9900",
		fillOpacity : 0.60
	});
	poiArea.set("id",id);
	
	poiArea.setMap(map);
	poisAreas.push(poiArea);
}

function getPoints(polygon) {
	var polygonSt = polygon.substring(9, polygon.length - 2);
	var points = [];
	var i = 0;
	var pos = polygonSt.indexOf(',');
	while (pos > 0) {
		var nextPoints = polygonSt.substring(0, pos);
		var iPoint = getPoint(nextPoints);
		points[i] = iPoint;
		i++;
		polygonSt = polygonSt.substring(pos + 1, polygonSt.length);
		var pos = polygonSt.indexOf(',');
	}
	var lastPoint = polygonSt;
	var lastPointLatLong = getPoint(lastPoint);
	points[i] = lastPointLatLong;
	return points;
}

function getPoint(point) {
	var pos = point.indexOf(' ');
	var x = point.substring(0, pos);
	var y = point.substring(pos + 1);
	return new google.maps.LatLng(x, y);
}

function criarBalaoDeInformacao(marker, nome, tipo, img){
	var window = '';
	if(img!=null)
		window = window + '<center><a href="#"><img src="'+img+'" width="60"/></a></center><br/>';
	window = window + '<b>Nome:</b> '+ nome +'<br/>'+
			'<b>Tipo:</b>'+ tipo +'<br/>'+
			'<div class="baloonmenu">'+
			'<a href="javascript:deletar('+marker.get("id")+')"><img class="baloonimg" alt="Deletar POI" title="Remover" src="img/delete.png"/></a>'+
			'<a href="javascript:editar('+marker.get("id")+')"><img alt="Editar POI" title="Editar" src="img/edit.png"/></a>'+
			'<a href="javascript:addImg('+marker.get("id")+')"><img alt="Adicionar Imagem" title="Adicionar" src="img/add-img.png"/></a>'+
			'<a href="javascript:drawShotArea('+marker.get("id")+')"><img alt="Desenhar Área" title="Adicionar Área" src="img/shot-area.png"/></a>'+
			'</div>';
	marker.info = new google.maps.InfoWindow({
		content: window
	});
	return marker;
}

function colocaMarcador(location) {
	var m = new google.maps.Marker({
		position : location,
		map : map,
		icon: "img/blue-marker.png"
	});
	return m;
}

function addPOI() {
	var valid = false;
	
	if(($("#nome").val().length > 0) && ($("#tipo").val().length > 0)){
		valid = true;
	}
	
	if (valid) {
		var nome = $("#nome").val();
		var tipo = $("#tipo").val();
		var lat = marker.getPosition().lat();
		var lng = marker.getPosition().lng();
		var json = 
			{
				name: nome,
				type: tipo,
				latitude: lat,
				longitude: lng
			};
		
		$.ajax({
			type : "POST",
			dataType: "json",
			contentType: "application/json",
			url : URL + "services/pois/send/",
			data : json,
			success : function(data, status) {
				if(data != 0){
					var m = marker;
					m.set("id",data);
					
					m = criarBalaoDeInformacao(m, nome, tipo, null);
					
					google.maps.event.addListener(m, 'click', function() {
						m.info.open(map, m);
					});
					
					markers.push(m);
					
					flagSaved = true;
				}
				dialog.dialog("close");
			},
			error : function OnExecuteError(request, status, error) {
				alert("Erro! " + error);
			}
		});
	}
	return valid;
}

function adicionarShotAreaAoPOI(id){
	for (var index = 0; index < markers.length; index++) {
		if(markers[index].get("id")==poiAdicionarShotArea){
			markers[index].info.close();
		}
	}
	listenerColocaMarcadorDeArea = google.maps.event.addListener(map, 'click', function(e){
		addPoint(e.latLng, map, id);
	});
}

function addPoint(latLng, map, id){
	
	var image = 'img/orange_point.png';
	var point = new google.maps.Marker({
		position: latLng,
		icon: image,
		map: map
	});
	
	listenerPrimeiroMarcadorDeArea = 
		google.maps.event.addListener(point, 'click', function drawPolygon(){
			if(point==points[0]){
				coordinates.push(coordinates[0]);
				polygon = new google.maps.Polygon({
					paths: coordinates,
					strokeColor: "#FF9900",
				    strokeOpacity: 0.8,
				    strokeWeight: 2,
				    fillColor: "#FF9900",
				    fillOpacity: 0.60
				});
				//enviar geometry para ws
				$.ajax({
		  	        type: "POST",
		  	        url: URL + "services/pois/addShotArea?poi_id="+id+"&area="+coordinates,
		  	        data: {},
		  	        success: function(data, status){
		  	        	polygon.setMap(map);
		  	        	polygon.set("id",id);
		  	        	poisAreas.push(polygon);
		  	        	
		  	        	google.maps.event.removeListener(listenerColocaMarcadorDeArea);
						
						for (var index = 0; index < points.length; index++) {
							points[index].setMap(null);
						}
						
						for (var i = 0; i < lines.length; i++) {
							lines[i].setMap(null);
						}
						
						lines = [];
						points = [];
						coordinates = [];
		  	        },
		  	        error: function OnExecuteError(request, status, error) {
		  	            confirm(error);
		  	        }
		  	    });
			}
		});
	
	points.push(point);
	
	coordinates.push(latLng);
	
	drawLine();
}

function drawLine(){
	if(firstDraw){
		line = new google.maps.Polyline({
			path: coordinates,
			strokeColor: '#FF9900',
		    strokeOpacity: 1.0,
		    strokeWeight: 2
		});
		line.setMap(map);
		lines.push(line);
		firstdraw = false;
	}else{
		line.setMap(null);
		firstDraw = true;
		drawLine();
	}
}

function deletar(id){
	poiParaRemocao = id;
	confirmDialog.dialog("open");
}

function editar(id){
	
}

function addImg(id){
	poiAdicionarFoto = id;
	dialogCarregaFoto.dialog("open");
}

function drawShotArea(id){
	poiAdicionarShotArea = id;
	dialogoMensagemShotArea.dialog("open");
}