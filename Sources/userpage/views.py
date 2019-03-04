from django.shortcuts import render, redirect
from userpage.models import Articles
from django.views.generic import ListView, DetailView


def add_task(request):
    if request.method == 'POST':
        title = request.POST.get('task')
        sence = request.POST.get('task_sence')
        date = request.POST.get('date')
        new_task = Articles(title, sence, date)
        new_task.save()
    return redirect('/user')
