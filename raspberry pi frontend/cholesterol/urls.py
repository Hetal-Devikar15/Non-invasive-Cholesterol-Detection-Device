from django.contrib import admin
from django.urls import path
from btapp import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path("", views.index, name="index"),
    path("qr/", views.qr_code, name="qr"),
    path("data/", views.sse_view, name="sse"),
]