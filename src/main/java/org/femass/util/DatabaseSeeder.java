package org.femass.util;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.femass.entity.Curso;
import org.femass.entity.Validacao;

@ApplicationScoped
public class DatabaseSeeder {


    @Transactional
    public void onStart(@Observes StartupEvent event){




    }


    private void CriarCursos(){

    }

}

