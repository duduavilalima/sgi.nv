package br.com.araujoabreu.sgi.resources.menu;

import br.com.araujoabreu.sgi.service.menu.MenuService;
import br.com.araujoabreu.sgi.service.menu.transfer.ModuloTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/menu")
public class MenuResource {

	@Autowired
	private MenuService menuService;

	@RequestMapping(method = RequestMethod.GET)
	public List<ModuloTO> obterMenu() {
		try {
			return menuService.getMenu();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
