package familyhealth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import familyhealth.model.Role;
import familyhealth.model.dto.RoleDTO;
import familyhealth.service.impl.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    final private RoleService roleService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRole(@PathVariable Long id){
        try{
            Role role = roleService.getRole(id);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> creatRole(@RequestBody RoleDTO roleDTO){
        try{
            Role role = roleService.createRole(roleDTO);
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id,
                                        @RequestBody RoleDTO roleDTO){
        try{
            Role role = roleService.updateRole(id, roleDTO);
            return ResponseEntity.ok("Update role : " + role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id){
        try{
            roleService.deleteRole(id);
            return ResponseEntity.ok("Delete role : " + id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
