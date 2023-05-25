package com.myprojects.apnamoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Versions> versionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        recyclerView = findViewById(R.id.recyclerView);
        initData();
        setRecyclerView();
    }

    private void setRecyclerView() {
        VersionsAdapter versionsAdapter = new VersionsAdapter(versionsList);
        recyclerView.setAdapter(versionsAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {
        versionsList = new ArrayList<>();
        versionsList.add(new Versions("How will we stay on track?","","Apna Money",
                "The budget review is critically important, but it also doesn't have to be super complicated or take a whole bunch of time each week."));
        versionsList.add(new Versions("Include saving in your budget"," ","Apna Money ",
                "Now that you know what you spent in a month, you can begin to create a budget. Your budget should show what your expenses are relative to your income, so that you can plan your spending and limit overspending. Be sure to factor in expenses that occur regularly."));
        versionsList.add(new Versions("Find ways to cut spending","","Apna Money",
                "If you can't save as much as you'd like, it might be time to cut back on expenses. Identify non essentials, such as entertainment and dining out, that you can spend less on. Look for ways to save on your fixed monthly expenses, such as your car insurance or cell phone plan"));
        versionsList.add(new Versions("Determine your financial priorities","","Apna Money",
                "After your expenses and income, your goals are likely to have the biggest impact on how you allocate your savings. For example, if you know you're going to need to replace your car in the near future, you could start putting away money for one now. But be sure to remember long term goals it's important that planning for retirement doesn't take a back seat to shorter term needs. Learning how to prioritize your saving goals can give you a clear idea of how to allocate your savings. "));
        versionsList.add(new Versions("How to budget a money?","","Apna Money",
                "Calculate your monthly income, pick a budgeting method and monitor your progress. \n\nTry the 50-30-20 rule as a simple budgeting framework. \n1)Allow upto 50 percent of your income or needs \n2)Leave 30 percent of your income for wants. \n3)Commit 20 percent of your income to savings and debt repayment.\n\n Track and manage your budget through regular check ins"));
        versionsList.add(new Versions("Evaluate Purchases by Cost Per Use"," ","Apna Money",
                "It may seem more financially responsible to buy a trendy $5 shirt than a basic $30 shirt—but only if you ignore the quality factor! When deciding if the latest tech toy, kitchen gadget, or apparel item is worth it, factor in how many times you’ll use it or wear it. For that matter, you can even consider cost per hour for experiences!"));
        versionsList.add(new Versions("Readjust and Revisit the plan accordingly"," ","Apna Money",
                "Income, expenses, wants, and desires are not stable. They keep on changing and evolving. So you need to make sure that you readjust and revisit your plan accordingly and make changes as and when required.\n\nYou can figure out by yourself the level of extensiveness required to understand the budget process.\n\nOnce you have got the command, you can hold it right!"));

    }
}