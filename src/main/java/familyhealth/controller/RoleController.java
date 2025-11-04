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

    @GetMapping("/{id}")
    public ResponseEntity<?> getRole(@PathVariable Long id){
        try{
            Role role = roleService.getRole(id);
            return ResponseEntity.ok("Get role " + role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> creatRole(@RequestBody RoleDTO roleDTO){
        try{
            Role role = roleService.createRole(roleDTO);
            return ResponseEntity.ok("Create role : " + role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id,
                                        @RequestBody RoleDTO roleDTO){
        try{
            Role role = roleService.updateRole(id, roleDTO);
            return ResponseEntity.ok("Update role : " + role);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id){
        try{
            return ResponseEntity.ok("Delete role : " + id);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
