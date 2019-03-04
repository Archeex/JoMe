from django.shortcuts import render, redirect
from django.contrib import auth
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth import authenticate, login
from django.contrib.auth.models import User


def register(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)
        if form.is_valid():
            form.save()
            username = request.POST.get('username')
            password = request.POST.get('password1')
            user = auth.authenticate(
                request,
                username = username,
                password = password
            )
            return redirect('/')
    else:
        form = UserCreationForm()
    args = {'form': form}
    return render(request, 'regandauth/register.html', args)


def user_login(request):
    args = {}
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        user = auth.authenticate(username=username, password=password)
        if user is not None:
            auth.login(request, user)
            args = {'username' : username}
            if username == 'admin':
                return redirect('/admin')
            else:
                return redirect('/user')
        else:
            args = {'login_error' : "User not found"}
    return render(request, 'regandauth/login.html', args)


def logout(request):
    auth.logout(request)
    return redirect('/')



