package com.example.projectsantander.login;

import com.example.projectsantander.services.LoginResponse;
import com.example.projectsantander.services.RetrofitApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginModelImpl implements LoginContract.LoginModel{

    private LoginContract.LoginPresenter presenter;

    public LoginModelImpl(LoginContract.LoginPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void realizaLogin(String usuario, String senha) {
        if(usuario == null || usuario.trim().equals("")){
            presenter.usuarioNaoInformado();
            return;
        }
        if(senha == null || senha.trim().equals("")){
            presenter.senhaNaoInformada();
            return;
        }
       presenter.loadingRequisicao();
        new RetrofitApi().getService().login(usuario, senha).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse resp = response.body();
                presenter.fechaLoading();
                if(resp.getError() == null || resp.getError().getCode() == 0){
                    presenter.loginCompleto(resp.getUserAccount());
                }
                else{
                   presenter.loginIncorreto(resp.getError().getMessage());
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                presenter.fechaLoading();
                presenter.erroServidor();
            }
        });
    }
}